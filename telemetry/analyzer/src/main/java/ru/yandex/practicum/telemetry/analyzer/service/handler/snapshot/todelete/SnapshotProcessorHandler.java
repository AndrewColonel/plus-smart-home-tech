package ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot.todelete;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface SnapshotProcessorHandler {

    String getRecordType();

    void handleRecord(SensorsSnapshotAvro event);

}
