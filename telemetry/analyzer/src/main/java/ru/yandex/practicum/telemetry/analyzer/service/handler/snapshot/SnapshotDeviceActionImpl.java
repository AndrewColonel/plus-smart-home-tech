package ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot;

import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Condition;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.ConditionOperation;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.ConditionType;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Scenario;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.SensorRepository;

import java.time.Instant;
import java.util.ArrayList;
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


//                            boolean conditionState;

//                            conditionOperationCheck(sensor,condition);

//                            switch (ConditionOperation.valueOf(condition.getOperation())) {
//                                case ConditionOperation.EQUALS -> {
//                                    conditionState = Integer.valueOf(
//                                            sensor.getMotion() ? 1 : 0).equals(condition.getValue());
//                                }
//                                case ConditionOperation.GREATER_THAN -> {
//                                    conditionState = (sensor.getMotion() ? 1 : 0) > (condition.getValue());
//                                }
//                                case ConditionOperation.LOWER_THAN -> {
//                                    conditionState = (sensor.getMotion() ? 1 : 0) < (condition.getValue());
//                                }
//                            }
//                            log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//                            log.debug("сенсор: {} типа: {}", idSensor, condition.getType());
//                            log.info("кондиция {} сценария {}", condition.getId(), checkScenario.getName());
//                            log.debug("пороговое значение: {}", condition.getValue());
//                            log.debug("условие: {}", condition.getOperation());
//                            log.debug("изменение: {}", sensor);
//                            log.info("выполниться: {}", conditionState);
//                            log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                        }
                        case ConditionType.TEMPERATURE -> {
                            // если тип кондиции TEMPERATURE, то выбранный по id из условий сценария сенсор температуры
                            if (sensorState.getData() instanceof TemperatureSensorAvro sensor) {
                                conditionOperationCheck(sensor.getTemperatureC(), condition,
                                        checkScenario.getName());

//                                TemperatureSensorAvro sensor = (TemperatureSensorAvro) sensorState.getData();
//                            boolean conditionState;
//                                switch (ConditionOperation.valueOf(condition.getOperation())) {
//                                    case ConditionOperation.EQUALS -> {
//                                        conditionState = Integer.valueOf(
//                                                sensor.getTemperatureC()).equals(condition.getValue());
//                                    }
//                                    case ConditionOperation.GREATER_THAN -> {
//                                        conditionState = sensor.getTemperatureC() > (condition.getValue());
//                                    }
//                                    case ConditionOperation.LOWER_THAN -> {
//                                        conditionState = sensor.getTemperatureC() < (condition.getValue());
//                                    }
//                                }
//                                log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//                                log.debug("сенсор: {} типа: {}", idSensor, condition.getType());
//                                log.info("кондиция {} сценария {}", condition.getId(), checkScenario.getName());
//                                log.debug("пороговое значение: {}", condition.getValue());
//                                log.debug("условие: {}", condition.getOperation());
//                                log.debug("изменение: {}", sensor);
//                                log.info("выполниться: {}", conditionState);
//                                log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");


                            } else {
                                ClimateSensorAvro sensor = (ClimateSensorAvro) sensorState.getData();

                                conditionOperationCheck(sensor.getTemperatureC(), condition,
                                        checkScenario.getName());
//                            boolean conditionState;
//                                switch (ConditionOperation.valueOf(condition.getOperation())) {
//                                    case ConditionOperation.EQUALS -> {
//                                        conditionState = Integer.valueOf(
//                                                sensor.getTemperatureC()).equals(condition.getValue());
//                                    }
//                                    case ConditionOperation.GREATER_THAN -> {
//                                        conditionState = sensor.getTemperatureC() > (condition.getValue());
//                                    }
//                                    case ConditionOperation.LOWER_THAN -> {
//                                        conditionState = sensor.getTemperatureC() < (condition.getValue());
//                                    }
//                                }
//                                log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//                                log.debug("сенсор: {} типа: {}", idSensor, condition.getType());
//                                log.info("кондиция {} сценария {}", condition.getId(), checkScenario.getName());
//                                log.debug("пороговое значение: {}", condition.getValue());
//                                log.debug("условие: {}", condition.getOperation());
//                                log.debug("изменение: {}", sensor);
//                                log.info("выполниться: {}", conditionState);
//                                log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                            }
                        }
                        case ConditionType.LUMINOSITY -> {
                            // если тип кондиции LUMINOSITY, то выбранный по id из условий сценария сенсор освещенности
                            LightSensorAvro sensor = (LightSensorAvro) sensorState.getData();
                            conditionOperationCheck(sensor.getLuminosity(), condition,
                                    checkScenario.getName());

//                            boolean conditionState;
//                            switch (ConditionOperation.valueOf(condition.getOperation())) {
//                                case ConditionOperation.EQUALS -> {
//                                    conditionState = Integer.valueOf(
//                                            sensor.getLuminosity()).equals(condition.getValue());
//                                }
//                                case ConditionOperation.GREATER_THAN -> {
//                                    conditionState = sensor.getLuminosity() > (condition.getValue());
//                                }
//                                case ConditionOperation.LOWER_THAN -> {
//                                    conditionState = sensor.getLuminosity() < (condition.getValue());
//                                }
//                            }
//                            log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//                            log.debug("сенсор: {} типа: {}", idSensor, condition.getType());
//                            log.info("кондиция {} сценария {}", condition.getId(), checkScenario.getName());
//                            log.debug("пороговое значение: {}", condition.getValue());
//                            log.debug("условие: {}", condition.getOperation());
//                            log.debug("изменение: {}", sensor);
//                            log.info("выполниться: {}", conditionState);
//                            log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                        }
                        case ConditionType.SWITCH -> {
                            // если тип кондиции SWITCH, то выбранный по id из условий сценария сенсор переключатель
                            SwitchSensorAvro sensor = (SwitchSensorAvro) sensorState.getData();
                            conditionOperationCheck((sensor.getState() ? 1 : 0), condition,
                                    checkScenario.getName());

//                            boolean conditionState;
//                            switch (ConditionOperation.valueOf(condition.getOperation())) {
//                                case ConditionOperation.EQUALS -> {
//                                    conditionState = Integer.valueOf(
//                                            sensor.getState() ? 1 : 0).equals(condition.getValue());
//                                }
//                                case ConditionOperation.GREATER_THAN -> {
//                                    conditionState = (sensor.getState() ? 1 : 0) > (condition.getValue());
//                                }
//                                case ConditionOperation.LOWER_THAN -> {
//                                    conditionState = (sensor.getState() ? 1 : 0) < (condition.getValue());
//                                }
//                            }
//                            log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//                            log.debug("сенсор: {} типа: {}", idSensor, condition.getType());
//                            log.info("кондиция {} сценария {}", condition.getId(), checkScenario.getName());
//                            log.debug("пороговое значение: {}", condition.getValue());
//                            log.debug("условие: {}", condition.getOperation());
//                            log.debug("изменение: {}", sensor);
//                            log.info("выполниться: {}", conditionState);
//                            log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                        }
                        // если не подошел ни один из перечисленных типов, то осталяс только климатический сенсор
                        default -> {
                            ClimateSensorAvro sensor = (ClimateSensorAvro) sensorState.getData();
//                            boolean conditionState;
                            if (ConditionType.valueOf(condition.getType()).equals(ConditionType.HUMIDITY)) {

                                conditionOperationCheck(sensor.getHumidity(), condition,
                                        checkScenario.getName());
//                                switch (ConditionOperation.valueOf(condition.getOperation())) {
//                                    case ConditionOperation.EQUALS -> {
//                                        conditionState = Integer.valueOf(
//                                                sensor.getHumidity()).equals(condition.getValue());
//                                    }
//                                    case ConditionOperation.GREATER_THAN -> {
//                                        conditionState = sensor.getHumidity() > (condition.getValue());
//                                    }
//                                    case ConditionOperation.LOWER_THAN -> {
//                                        conditionState = sensor.getHumidity() < (condition.getValue());
//                                    }
//
//                                }
//                                log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//                                log.debug("сенсор: {} типа: {}", idSensor, condition.getType());
//                                log.info("кондиция {} сценария {}", condition.getId(), checkScenario.getName());
//                                log.debug("пороговое значение: {}", condition.getValue());
//                                log.debug("условие: {}", condition.getOperation());
//                                log.debug("изменение: {}", sensor);
//                                log.info("выполниться: {}", conditionState);
//                                log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");


                            } else {
                                conditionOperationCheck(sensor.getCo2Level(), condition,
                                        checkScenario.getName());
//
//                                switch (ConditionOperation.valueOf(condition.getOperation())) {
//                                    case ConditionOperation.EQUALS -> {
//                                        conditionState = Integer.valueOf(
//                                                sensor.getCo2Level()).equals(condition.getValue());
//                                    }
//                                    case ConditionOperation.GREATER_THAN -> {
//                                        conditionState = sensor.getCo2Level() > (condition.getValue());
//                                    }
//                                    case ConditionOperation.LOWER_THAN -> {
//                                        conditionState = sensor.getCo2Level() < (condition.getValue());
//                                    }
//                                }
//                                log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//                                log.debug("сенсор: {} типа: {}", idSensor, condition.getType());
//                                log.info("кондиция {} сценария {}", condition.getId(), checkScenario.getName());
//                                log.debug("пороговое значение: {}", condition.getValue());
//                                log.debug("условие: {}", condition.getOperation());
//                                log.debug("изменение: {}", sensor);
//                                log.info("выполниться: {}", conditionState);
//                                log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                            }
                        }
                    }
//                    log.debug("сенсор: {} типа: {}", idSensor, condition.getType());
//                    log.debug("пороговое значение: {}", condition.getValue());
//                    log.debug("условие: {}", condition.getOperation());
//                    log.debug("изменение: {}", );
//
//                    log.debug("выполниться: {}", conditionState);


//                    scenarioConditionState = scenarioConditionState|| conditionState

//                    if (sensorState.getData() instanceof ClimateSensorAvro climateSensorAvro) {
//                        // климатический сенсор передает три параметра - температура, влажность, содержание CO2
//                        // состояние из сценария, соотвесвующее климатическому датчику несет только
//                        // один параметр состояния из трех - температура, влажность, содержание CO2
//                        // необходимо проверить на сработку  все три случая
//
////                        boolean coditionState;
//                        switch (ConditionType.valueOf(condition.getType())) {
//                            case ConditionType.TEMPERATURE -> {
//                                //если в сценарии пришло описание состояния по температуре
//
//                                // вычисляем срабатывание сценария по заданному условию
//                                switch (ConditionOperation.valueOf(condition.getOperation())) {
//                                    case ConditionOperation.EQUALS -> {
//                                        coditionState =
//                                                Integer.valueOf(
//                                                     climateSensorAvro.getTemperatureC()).equals(condition.getValue());
//                                   }
//                                    case ConditionOperation.GREATER_THAN -> {
//                                        coditionState =
//                                                climateSensorAvro.getTemperatureC() > condition.getValue();
//                                    }
//                                    case ConditionOperation.LOWER_THAN -> {
//                                        coditionState =
//                                                climateSensorAvro.getTemperatureC() < condition.getValue();
//                                    }
//
//                                }
//                                climateSensorAvro.getTemperatureC();
//                                condition.getValue();
////                                condition.getOperation();
//                            }
//                            //если в сценарии пришло описание состояния по CO2
//                            case ConditionType.CO2LEVEL -> {
//
//                            }
//                            //если в сценарии пришло описание состояния по влажности
//                            case ConditionType.HUMIDITY -> {
//
//                            }
//                        }
//
//
//                        climateSensorAvro.getHumidity();
//                        climateSensorAvro.getCo2Level();
//
//                    }
//
//
//                    if (sensorState.getData() instanceof LightSensorAvro lightSensorAvro) {
//
//                    }
//                    if (sensorState.getData() instanceof MotionSensorAvro motionSensorAvro) {
//
//                    }
//                    if (sensorState.getData() instanceof SwitchSensorAvro switchSensorAvro) {
//
//                    }
//                    if (sensorState.getData() instanceof TemperatureSensorAvro temperatureSensorAvro) {
//
//                    }
//
////                    switch (condition.getType()) {
////                        case "MOTION":
////                            break;
////                    }


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

    public boolean conditionOperationCheck(int sensorValue, Condition condition, String scenarioName) {
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


//private int operationConverter(ConditionType type) {
//        return switch (type) {
//            case ConditionType.MOTION ->
//        }
//}


    private void sendRequest(DeviceActionRequest request) {
        log.info("Отправляю данные: {}", request.getAllFields());
        Empty response = hubRouterClient.handleDeviceAction(request);
    }


}
