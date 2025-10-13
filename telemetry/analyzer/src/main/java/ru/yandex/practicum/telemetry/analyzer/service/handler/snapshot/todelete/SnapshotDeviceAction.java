package ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot.todelete;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface SnapshotDeviceAction {

    void handleScenario(SensorsSnapshotAvro event);

}
