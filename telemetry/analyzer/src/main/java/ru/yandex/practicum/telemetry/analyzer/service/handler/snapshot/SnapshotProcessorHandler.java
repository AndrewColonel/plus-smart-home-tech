package ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Scenario;

import java.util.List;
import java.util.Optional;

public interface SnapshotProcessorHandler {

    String getRecordType();

    Optional<List<Scenario>> handleScenario(SensorsSnapshotAvro event);

}
