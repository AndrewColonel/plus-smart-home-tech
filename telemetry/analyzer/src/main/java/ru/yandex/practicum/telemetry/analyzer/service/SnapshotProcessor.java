package ru.yandex.practicum.telemetry.analyzer.service;

import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConfig;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Condition;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Scenario;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.SensorRepository;
import ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot.SnapshotProcessorHandler;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SnapshotProcessor extends BaseProcessor {

    private final Map<String, SnapshotProcessorHandler> snapshotProcessorHandlers;

    private final DeviceActionRequestProducer deviceActionRequestProducer;

    private final SensorRepository sensorRepository;

    private final ScenarioRepository scenarioRepository;

    @Autowired
    public SnapshotProcessor(KafkaConfig kafkaConfig, List<SnapshotProcessorHandler> snapshotProcessorHandlers, DeviceActionRequestProducer deviceActionRequestProducer,
                             SensorRepository sensorRepository, ScenarioRepository scenarioRepository) {

        super(kafkaConfig.getSnapshotConsumer().getProperties(),
                kafkaConfig.getSnapshotConsumer().getTopic(),
                kafkaConfig.getSnapshotConsumer().getPollTimeout());
        this.deviceActionRequestProducer = deviceActionRequestProducer;
        this.sensorRepository = sensorRepository;
        this.scenarioRepository = scenarioRepository;
        this.snapshotProcessorHandlers = snapshotProcessorHandlers.stream()
                .collect(Collectors.toMap(SnapshotProcessorHandler::getRecordType,
                        Function.identity()));
    }

    @Override
    public void handleRecord(ConsumerRecord<String, SpecificRecordBase> record) {
        log.trace("<<< Получено сообщение топика = {}, партиция = {}, смещение = {}, значение: {}\n",
                record.topic(), record.partition(), record.offset(), record.value());
        log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        log.debug("Получен снапшот: {}", record.value());
        log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        if (record.value() instanceof SensorsSnapshotAvro event) {

            // получаем id хаба и список id сенсоров из снапшота
            String hubId = event.getHubId();
            List<String> sensorIds = event.getSensorsState().keySet().stream().toList();
            // проверяем соответсвует ли списко сенсоров данному хабу
//            if (sensorRepository.existsByIdInAndHubId(sensorIds, hubId)) {
            List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);
            if (!scenarios.isEmpty()) {
                log.trace("Найдены сценарии {} использующие сенсоры из снапшота {}",
                        scenarios, hubId);
                // сценарий стоит рассматиривать только если все задействованные сенсоры  передали данные в снапшоте
                // проверить - все ли сенсоры (id) состояния из сценария, содержатся в снапшоте,
                List<Scenario> checkScenarios = scenarios.stream()
                        .filter(scenario ->
                                event.getSensorsState().keySet()
                                        .containsAll(scenario.getConditions().keySet()))
                        .toList();
                log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                log.debug("Список сценариев, который будет проверяться на срабатывание {}", checkScenarios);
                log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                // далее необходимо проверить все состояния каждого сценария на сработку по снапшоту
                // и получить список сценариев на исполнение
                // сценарий сработает, только если все условия выполнены

                List<Scenario> activeScenarios = checkScenarios.stream()
                        .filter(scenario -> {
                            // сценарий не сработает, если хотя бы одна кондиция не сработает по эвенту из снапшота
                            boolean conditionState = false;
                            for (Map.Entry<String, Condition> entry : scenario.getConditions().entrySet()) {
                                String idSensor = entry.getKey();
                                Condition condition = entry.getValue();

                                SensorStateAvro sensorState = event.getSensorsState().get(idSensor);

                                // ----------------------------------------------------------------------

                                String handlerName = sensorState.getData().getClass().getSimpleName();
                                SnapshotProcessorHandler handler = snapshotProcessorHandlers.get(handlerName);
                                if (Objects.nonNull(handler)) {
                                    log.trace("Выбран обработчик {}", handler.getClass().getSimpleName());
                                    conditionState = handler.handleScenario(sensorState, condition,
                                            idSensor, scenario.getName());
                                } else {
                                    log.trace("Обработчика для {} не найдено", handlerName);
                                }
                            }
                            return conditionState;
                        })
                        .peek(scenario -> {
                                    log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                                    log.info("Сценарий сработал, все кондиции изменились в снапшоте");
                                    log.info("сценарий {}", scenario.getName());
                                    log.debug("кондиции {}", scenario.getConditions());
                                    log.debug("действия: {}", scenario.getActions());
                                    log.debug("количество действий: {}", scenario.getActions().size());
                                    log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                                }
                        )
                        .toList();
                log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                log.debug("Отклонено сценариев: {}", checkScenarios.size() - activeScenarios.size());
                log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                // если списко проверенных сценариев не пуст - отправляем запрос на хаб
                if (!activeScenarios.isEmpty()) sendRequest(activeScenarios);
            }
        }
    }

    private void sendRequest(List<Scenario> activeScenarios) {
        // пройтись по списку таких сценариев, создаая DeviceActionRequest и отправляя их в Hub Router
        for (Scenario activeScenario : activeScenarios) {
            activeScenario.getActions().forEach((idSensor, action) -> {
                DeviceActionProto deviceActionProto = DeviceActionProto.newBuilder()
                        .setSensorId(idSensor)
                        .setType(ActionTypeProto.valueOf(action.getType().trim().toUpperCase()))
                        .setValue(action.getValue())
                        .build();
                DeviceActionRequest deviceActionRequest = DeviceActionRequest.newBuilder()
                        .setHubId(activeScenario.getHubId())
                        .setScenarioName(activeScenario.getName())
                        .setAction(deviceActionProto)
                        .setTimestamp(Timestamp.newBuilder()
                                .setSeconds(Instant.now().getEpochSecond())
                                .setNanos(Instant.now().getNano())
                                .build())
                        .build();
                log.debug("----------------------------------------------------------------------");
                log.info("Запрос на отправку:");
                log.info("сценарий - {}", deviceActionRequest.getScenarioName());
                log.debug("устройство - {}", deviceActionRequest.getAction().getSensorId());
                log.debug("действие - {}", deviceActionRequest.getAction().getType());
                log.debug("величина - {}", deviceActionRequest.getAction().getValue());
                log.debug("----------------------------------------------------------------------");

                deviceActionRequestProducer.send(deviceActionRequest);
            });
        }
    }
}
