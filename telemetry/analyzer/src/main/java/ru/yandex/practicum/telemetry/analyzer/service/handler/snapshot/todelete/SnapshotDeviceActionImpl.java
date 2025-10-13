package ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot.todelete;

import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Condition;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.ConditionOperation;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.ConditionType;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Scenario;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.SensorRepository;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class SnapshotDeviceActionImpl implements SnapshotDeviceAction {

    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    private final SensorRepository sensorRepository;

    private final ScenarioRepository scenarioRepository;

    public SnapshotDeviceActionImpl(@GrpcClient("hub-router")
                                    HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient,
                                    SensorRepository sensorRepository, ScenarioRepository scenarioRepository) {
        this.hubRouterClient = hubRouterClient;
        this.sensorRepository = sensorRepository;
        this.scenarioRepository = scenarioRepository;
    }

    public void handleScenario(SensorsSnapshotAvro event) {

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
            boolean scenarioConditionState = false;
            for (Scenario checkScenario : checkScenarios) {
                checkScenario.getConditions().forEach((idSensor, condition) -> {
                    SensorStateAvro sensorState = event.getSensorsState().get(idSensor);

                    // ----------------------------------------------------------------------
                    boolean conditionState = false;
                    switch (ConditionType.valueOf(condition.getType())) {
                        case ConditionType.MOTION -> {
                            // если тип кондиции MOTION, то выбранный по id из условий сценария сенсор на движение
                            MotionSensorAvro sensor = (MotionSensorAvro) sensorState.getData();
                            // проверяю условие срабатывания по сценарию для этой кондиции
                            conditionState = conditionOperationCheck((sensor.getMotion() ? 1 : 0), condition,
                                    checkScenario.getName());
                        }
                        case ConditionType.TEMPERATURE -> {
                            // если тип кондиции TEMPERATURE, то выбранный по id из условий сценария сенсор температуры
                            // или климатический сенсор
                            if (sensorState.getData() instanceof TemperatureSensorAvro sensor) {
                                conditionState = conditionOperationCheck(sensor.getTemperatureC(), condition,
                                        checkScenario.getName());
                            } else {
                                ClimateSensorAvro sensor = (ClimateSensorAvro) sensorState.getData();

                                conditionState = conditionOperationCheck(sensor.getTemperatureC(), condition,
                                        checkScenario.getName());
                            }
                        }
                        case ConditionType.LUMINOSITY -> {
                            // если тип кондиции LUMINOSITY, то выбранный по id из условий сценария сенсор освещенности
                            LightSensorAvro sensor = (LightSensorAvro) sensorState.getData();
                            conditionState = conditionOperationCheck(sensor.getLuminosity(), condition,
                                    checkScenario.getName());
                        }
                        case ConditionType.SWITCH -> {
                            // если тип кондиции SWITCH, то выбранный по id из условий сценария сенсор переключатель
                            SwitchSensorAvro sensor = (SwitchSensorAvro) sensorState.getData();
                            conditionState = conditionOperationCheck((sensor.getState() ? 1 : 0), condition,
                                    checkScenario.getName());
                        }
                        // если не подошел ни один из перечисленных типов, то остался только климатический сенсор
                        default -> {
                            ClimateSensorAvro sensor = (ClimateSensorAvro) sensorState.getData();
                            if (ConditionType.valueOf(condition.getType()).equals(ConditionType.HUMIDITY)) {

                                conditionState = conditionOperationCheck(sensor.getHumidity(), condition,
                                        checkScenario.getName());
                            } else {
                                conditionState = conditionOperationCheck(sensor.getCo2Level(), condition,
                                        checkScenario.getName());
                            }
                        }
                    }
                    //------------------------------------------------------------------------------
                });
                log.debug("сценарий-{}- выполнится: {}", checkScenario.getName(), scenarioConditionState);
            }

            List<Scenario> activeScenarios = checkScenarios.stream().toList();
            // пройтись по списку таких сценариев, создаая DeviceActionRequest и отправляя их в Hub Router
            for (Scenario activeScenario : activeScenarios) {
                activeScenario.getActions().forEach((idSensor, action) -> {
                    DeviceActionProto deviceActionProto = DeviceActionProto.newBuilder()
                            .setSensorId(idSensor)
                            .setType(ActionTypeProto.valueOf(action.getType().trim().toUpperCase()))
                            .setValue(action.getValue())
                            .build();
                    DeviceActionRequest deviceActionRequest = DeviceActionRequest.newBuilder()
                            .setHubId(hubId)
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
                    sendRequest(deviceActionRequest);
                });
            }
        } else {
            log.info("таких сенсоров - {} - нет на хабе - {}", sensorIds, hubId);
        }
    }

    private boolean conditionOperationCheck(int sensorValue, Condition condition, String scenarioName) {
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
        log.debug("сенсор: {} типа: {}", condition.getId(), condition.getType());
        log.info("кондиция {} сценария {}", condition.getId(), scenarioName);
        log.debug("пороговое значение: {}", condition.getValue());
        log.debug("условие: {}", condition.getOperation());
        log.debug("изменение: {}", sensorValue);
        log.info("выполниться: {}", conditionState);
        log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        return conditionState;
    }

    private void sendRequest(DeviceActionRequest request) {
        log.info("Отправляю данные: {}", request.getAllFields());
        Empty response = hubRouterClient.handleDeviceAction(request);
    }


}
