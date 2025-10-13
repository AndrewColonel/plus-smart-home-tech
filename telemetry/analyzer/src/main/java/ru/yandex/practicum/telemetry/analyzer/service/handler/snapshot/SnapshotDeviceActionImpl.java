package ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot;

import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Scenario;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.SensorRepository;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class SnapshotDeviceActionImpl implements SnapshotDeviceAction {

//    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    @GrpcClient("hub-router")
    private HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    private final SensorRepository sensorRepository;

    private final ScenarioRepository scenarioRepository;

    public SnapshotDeviceActionImpl(
            SensorRepository sensorRepository, ScenarioRepository scenarioRepository) {
        this.sensorRepository = sensorRepository;
        this.scenarioRepository = scenarioRepository;
    }

//
//    public SnapshotDeviceActionImpl(@GrpcClient("hub-router")
//                                    HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient,
//                                    SensorRepository sensorRepository, ScenarioRepository scenarioRepository) {
//        this.hubRouterClient = hubRouterClient;
//        this.sensorRepository = sensorRepository;
//        this.scenarioRepository = scenarioRepository;
//    }
//


    //    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    public void handleScenario(SensorsSnapshotAvro event) {

        // получаем id хаба и список id сенсоров из снапшота
        String hubId = event.getHubId();
        List<String> sensorIds = event.getSensorsState().keySet().stream().toList();
        // проверяем соответсвует ли списко сенсоров данному хабу
        if (sensorRepository.existsByIdInAndHubId(sensorIds, hubId)) {
            List<Scenario> scenarios = scenarioRepository.findByHubId(hubId);
            log.trace("Найдены сценарии {} использующие сенсоры из снапшота {}",
                    scenarios, hubId);
            // сценарий сработает только если все задействованные сенсоры  передали данные в снапшоте
            // проверить - все сенсоры (id) состояния из сценария содержатся в снапшоте,
            List<Scenario> activeScenarios = scenarios.stream()
                    .filter(scenario ->
                            event.getSensorsState().keySet()
                                    .containsAll(scenario.getConditions().keySet()))
                    .toList();
            log.info("@@@@@@ Список сценариев, который будет проверяться на срабатывание {}", activeScenarios);

            // далее необходимо проверить все состояния каждого сценария на сработку

            // получить список сценариев на исполнение


            // пройтись по списку таких сценариев, создаая DeviceActionRequest и отправляя их в Hub Router
            for (Scenario activeScenario : activeScenarios) {
                activeScenario.getActions().forEach((key, value) -> {
                    DeviceActionProto deviceActionProto = DeviceActionProto.newBuilder()
                            .setSensorId(key)
                            .setType(ActionTypeProto.valueOf(value.getType()))
                            .setValue(value.getValue())
                            .build();

                    DeviceActionRequest deviceActionRequest = DeviceActionRequest.newBuilder()
                            .setHubId(hubId)
                            .setScenarioName(activeScenario.getName())
                            .setAction(deviceActionProto)
                            .setTimestamp( Timestamp.newBuilder()
                                    .setSeconds(Instant.now().getEpochSecond())
                                    .setNanos(Instant.now().getNano())
                                    .build())
                            .build();

                    sendRequest(deviceActionRequest);
                });
            }
        }
    }


    private void sendRequest(DeviceActionRequest request) {
        log.info("Отправляю данные: {}", request.getAllFields());
//        CollectorResponse response = collectorStub.collectSensorEvent(event);
        Empty response = hubRouterClient.handleDeviceAction(request);
//        log.info("Получил ответ от коллектора: {}", response);
    }


}
