package ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot;

import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Condition;

public interface SnapshotProcessorHandler {

    String getRecordType();

    boolean handleScenario(SensorStateAvro sensorState, Condition condition,
                           String idSensor, String scenarioName);

}
