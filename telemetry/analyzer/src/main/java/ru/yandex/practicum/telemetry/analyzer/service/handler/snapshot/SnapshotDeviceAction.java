package ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface SnapshotDeviceAction {

    void handleAction(SensorsSnapshotAvro event);

}
