package ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot.todelete;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

@Slf4j
@Component
@RequiredArgsConstructor
public class MotionProceessorHandler implements SnapshotProcessorHandler {

    @Override
    public String getRecordType() {
        return "MotionSensorAvro";
    }

    @Override
    public void handleRecord(SensorsSnapshotAvro event) {
        log.info("Начинаю регулеровку по датчикам движения");

    }
}
