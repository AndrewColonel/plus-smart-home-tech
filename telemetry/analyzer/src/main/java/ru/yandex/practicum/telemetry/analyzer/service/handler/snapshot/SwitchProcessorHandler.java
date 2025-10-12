package ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.service.handler.SnapshotProcessorHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class SwitchProcessorHandler implements SnapshotProcessorHandler {


    @Override
    public String getRecordType() {
        return "SwitchSensorAvro";
    }

    @Override
    public void handleRecord(SensorsSnapshotAvro event) {
        log.info("Начинаю регулеровку выключателей");

    }
}
