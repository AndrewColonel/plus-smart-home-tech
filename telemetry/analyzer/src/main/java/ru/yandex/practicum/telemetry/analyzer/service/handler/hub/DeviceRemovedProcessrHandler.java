package ru.yandex.practicum.telemetry.analyzer.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Sensor;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.SensorRepository;
import ru.yandex.practicum.telemetry.analyzer.service.handler.HubProcessorHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceRemovedProcessrHandler implements HubProcessorHandler {

    private final SensorRepository sensorRepository;

    @Override
    public String getRecordType() {
        return "DeviceRemovedEventAvro";
    }

    @Override
    public void handleRecord(HubEventAvro event) {
        DeviceRemovedEventAvro deviceRemovedEventAvro = (DeviceRemovedEventAvro) event.getPayload();
        Sensor sensor = Sensor.builder()
                .id(deviceRemovedEventAvro.getId())
                .hubId(event.getHubId())
                .build();
        sensorRepository.findByIdAndHubId(sensor.getId(), sensor.getHubId()).ifPresentOrElse(
                v -> {
                    sensorRepository.delete(v);
                    log.info("Удалено устройство {}", v);
                }, () -> log.info("Не найдено устройство {}", sensor));
    }
}
