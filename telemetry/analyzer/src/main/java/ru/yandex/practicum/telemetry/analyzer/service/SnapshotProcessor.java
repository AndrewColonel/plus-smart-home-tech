package ru.yandex.practicum.telemetry.analyzer.service;

import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConfig;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Condition;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.ConditionOperation;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.ConditionType;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Scenario;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.SensorRepository;
import ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot.SnapshotProcessorHandler;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SnapshotProcessor extends BaseProcessor {

    //    private final SnapshotDeviceActionImpl snapshotDeviceAction;
    private final Map<String, SnapshotProcessorHandler> snapshotProcessorHandlers;

    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;


    private final SensorRepository sensorRepository;

    private final ScenarioRepository scenarioRepository;

    @Autowired
    public SnapshotProcessor(@GrpcClient("hub-router")
                             HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient,
                             KafkaConfig kafkaConfig, List<SnapshotProcessorHandler> snapshotProcessorHandlers,
                             SensorRepository sensorRepository, ScenarioRepository scenarioRepository) {

        super(kafkaConfig.getSnapshotConsumer().getProperties(),
                kafkaConfig.getSnapshotConsumer().getTopic(),
                kafkaConfig.getSnapshotConsumer().getPollTimeout());
//        this.snapshotDeviceAction = snapshotDeviceAction;
        this.hubRouterClient = hubRouterClient;
        this.sensorRepository = sensorRepository;
        this.scenarioRepository = scenarioRepository;
        this.snapshotProcessorHandlers = snapshotProcessorHandlers.stream()
                .collect(Collectors.toMap(SnapshotProcessorHandler::getRecordType,
                        Function.identity()));
    }

    @Override
    public void handleRecord(ConsumerRecord<String, SpecificRecordBase> record) {
        log.debug("<<< Получено сообщение топика = {}, партиция = {}, смещение = {}, значение: {}\n",
                record.topic(), record.partition(), record.offset(), record.value());
        log.info("+++ Получен снапшот: +++ {}", record.value());
        if (record.value() instanceof SensorsSnapshotAvro event) {

            // получаем id хаба и список id сенсоров из снапшота
            String hubId = event.getHubId();
            List<String> sensorIds = event.getSensorsState().keySet().stream().toList();
            // проверяем соответсвует ли списко сенсоров данному хабу
            if (sensorRepository.existsByIdInAndHubId(sensorIds, hubId)) {
                List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);
                log.trace("Найдены сценарии {} использующие сенсоры из снапшота {}",
                        scenarios, hubId);
                // сценарий стоит рассматиривать только если все задействованные сенсоры  передали данные в снапшоте
                // проверить - все ли сенсоры (id) состояния из сценария, содержатся в снапшоте,
                List<Scenario> checkScenarios = scenarios.stream()
                        .filter(scenario ->
                                event.getSensorsState().keySet()
                                        .containsAll(scenario.getConditions().keySet()))
                        .toList();
                log.debug("Список сценариев, который будет проверяться на срабатывание {}", checkScenarios);

                // далее необходимо проверить все состояния каждого сценария на сработку по снапшоту
                // и получить список сценариев на исполнение
                // сценарий сработает, только если все условия выполнены

                List<Scenario> activeScenarios = checkScenarios.stream()
                        .filter(scenario -> {
                            boolean conditionState = false;
                            for (Map.Entry<String, Condition> entry : scenario.getConditions().entrySet()) {
                                String idSensor = entry.getKey();
                                Condition condition = entry.getValue();

                                SensorStateAvro sensorState = event.getSensorsState().get(idSensor);

                                // ----------------------------------------------------------------------
//
                                if (sensorState.getData() instanceof ClimateSensorAvro sensor) {
                                    switch (ConditionType.valueOf(condition.getType())) {
                                        case ConditionType.TEMPERATURE -> {
                                            conditionState = conditionOperationCheck(sensor.getTemperatureC(), condition,
                                                    idSensor, scenario.getName());
                                        }
                                        case ConditionType.CO2LEVEL -> {
                                            conditionState = conditionOperationCheck(sensor.getCo2Level(), condition,
                                                    idSensor, scenario.getName());
                                        }
                                        case ConditionType.HUMIDITY -> {
                                            conditionState = conditionOperationCheck(sensor.getHumidity(), condition,
                                                    idSensor, scenario.getName());
                                        }

                                    }
                                }
                                if (sensorState.getData() instanceof LightSensorAvro sensor) {
                                    conditionState = conditionOperationCheck(sensor.getLuminosity(), condition,
                                            idSensor, scenario.getName());
                                }
                                if (sensorState.getData() instanceof MotionSensorAvro sensor) {
                                    conditionState = conditionOperationCheck((sensor.getMotion() ? 1 : 0), condition,
                                            idSensor,scenario.getName());
                                }
                                if (sensorState.getData() instanceof SwitchSensorAvro sensor) {
                                    conditionState = conditionOperationCheck((sensor.getState() ? 1 : 0), condition,
                                            idSensor,scenario.getName());
                                }
                                if (sensorState.getData() instanceof TemperatureSensorAvro sensor) {
                                    conditionState = conditionOperationCheck(sensor.getTemperatureC(), condition,
                                            idSensor,scenario.getName());
                                }

                                //------------------------------------------------------------------------------
                            }
                            return conditionState;
                        })
                        .peek(scenario -> {
                                    log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                                    log.debug("Сценарий сработал, все кондиции изменились в снапшоте");
                                    log.info("сценарий {}", scenario.getName());
                                    log.debug("кондиции {}", scenario.getConditions());
                                    log.debug("действия: {}", scenario.getActions());
                                    log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                                }

                        )
                        .toList();
                log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                log.debug("Отклонено сценариев: {}", checkScenarios.size() - activeScenarios.size());
                log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

//                for (Scenario checkScenario : checkScenarios) {
//                    checkScenario.getConditions().forEach((idSensor, condition) -> {
//                        SensorStateAvro sensorState = event.getSensorsState().get(idSensor);
//
//                        // ----------------------------------------------------------------------
//                        boolean conditionState = false;
//
//                        if (sensorState.getData() instanceof ClimateSensorAvro sensor) {
//                            switch (ConditionType.valueOf(condition.getType())) {
//                                case ConditionType.TEMPERATURE -> {
//                                    conditionState = conditionOperationCheck(sensor.getTemperatureC(), condition,
//                                            checkScenario.getName());
//                                }
//                                case ConditionType.CO2LEVEL -> {
//                                    conditionState = conditionOperationCheck(sensor.getCo2Level(), condition,
//                                            checkScenario.getName());
//                                }
//                                case ConditionType.HUMIDITY -> {
//                                    conditionState = conditionOperationCheck(sensor.getHumidity(), condition,
//                                            checkScenario.getName());
//                                }
//
//                            }
//                        }
//                        if (sensorState.getData() instanceof LightSensorAvro sensor) {
//                            conditionState = conditionOperationCheck(sensor.getLuminosity(), condition,
//                                    checkScenario.getName());
//                        }
//                        if (sensorState.getData() instanceof MotionSensorAvro sensor) {
//                            conditionState = conditionOperationCheck((sensor.getMotion() ? 1 : 0), condition,
//                                    checkScenario.getName());
//                        }
//                        if (sensorState.getData() instanceof SwitchSensorAvro sensor) {
//                            conditionState = conditionOperationCheck((sensor.getState() ? 1 : 0), condition,
//                                    checkScenario.getName());
//                        }
//                        if (sensorState.getData() instanceof TemperatureSensorAvro sensor) {
//                            conditionState = conditionOperationCheck(sensor.getTemperatureC(), condition,
//                                    checkScenario.getName());
//                        }
//
//
//                        //------------------------------------------------------------------------------
//                    });
////                    log.debug("сценарий-{}- выполнится: {}", checkScenario.getName());
//                }

                if (!activeScenarios.isEmpty()) sendRequest(activeScenarios);

            }


//            String handlerName = event.getSpecificData().getClass().getSimpleName();
//            SnapshotProcessorHandler handler = snapshotProcessorHandlers.get(handlerName);
//            if (Objects.nonNull(handler)) {
//                log.debug("Выбран обработчик {}", handler.getClass().getSimpleName());
//                handler.handleScenario(event).ifPresentOrElse(this::sendRequest,
//                        () -> {
//                            log.info("для данного снапшота {}, нет активных сценариев", event);
//                        }
//                );
//            } else {
//                log.debug("Обработчика для {} не найдено", handlerName);
//            }
//


        }


    }


    public boolean conditionOperationCheck(int sensorValue, Condition condition,String idSensor, String scenarioName) {
        boolean conditionState = false;
        switch (ConditionOperation.valueOf(condition.getOperation())) {

            case ConditionOperation.EQUALS -> {
                conditionState = Integer.valueOf(sensorValue).equals(condition.getValue());
            }
            case ConditionOperation.GREATER_THAN -> {
                conditionState = (sensorValue) > (condition.getValue());
            }
            case ConditionOperation.LOWER_THAN -> {
                conditionState = (sensorValue) < (condition.getValue());
            }
        }
        log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        log.debug("Проверка кондиции");
        log.debug("сенсор: {} типа: {}", idSensor, condition.getType());
        log.info("кондиция {} сценария {}", condition.getId(), scenarioName);
        log.debug("пороговое значение: {}", condition.getValue());
        log.debug("условие: {}", condition.getOperation());
        log.debug("изменение: {}", sensorValue);
        log.info("выполниться: {}", conditionState);
        log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        return conditionState;
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
                log.debug("Запрос на отправку:");
                log.debug("сценарий - {}", deviceActionRequest.getScenarioName());
                log.debug("устройство - {}", deviceActionRequest.getAction().getSensorId());
                log.debug("действие - {}", deviceActionRequest.getAction().getType());
                log.debug("величина - {}", deviceActionRequest.getAction().getValue());
                log.debug("----------------------------------------------------------------------");

                Empty response = hubRouterClient.handleDeviceAction(deviceActionRequest);

            });
        }
    }
}
