package ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Scenario;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MotionProceessorHandler implements SnapshotProcessorHandler {

    @Override
    public String getRecordType() {
        return "MotionSensorAvro";
    }

    @Override
    public Optional<List<Scenario>> handleScenario(SensorsSnapshotAvro event) {
        log.info("Начинаю регулеровку по датчикам движения");

        return Optional.empty();
    }
}
