package ru.yandex.practicum.telemetry.analyzer.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Sensor;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.SensorRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceAddedProcessorHandler implements HubProcessorHandler {

    private final SensorRepository sensorRepository;

    @Override
    public String getRecordType() {
        return "DeviceAddedEventAvro";
    }

    @Override
    public void handleRecord(HubEventAvro event) {
        DeviceAddedEventAvro deviceAddedEventAvro = (DeviceAddedEventAvro) event.getPayload();
        Sensor sensor = Sensor.builder()
                .id(deviceAddedEventAvro.getId())
                .hubId(event.getHubId())
                .build();
        sensorRepository.findByIdAndHubId(sensor.getId(), sensor.getHubId()).ifPresentOrElse(
                d -> log.info("Такое устройство {} уже подключено", d),
                () -> {
                    sensorRepository.save(sensor);
                    log.info("Подключено устройство {}", sensor);
                });
    }
}
