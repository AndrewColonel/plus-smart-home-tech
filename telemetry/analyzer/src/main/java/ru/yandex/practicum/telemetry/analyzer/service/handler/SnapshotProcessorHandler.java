package ru.yandex.practicum.telemetry.analyzer.service.handler;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface SnapshotProcessorHandler {

    String getRecordType();

    void handleRecord(SensorsSnapshotAvro event);

}
