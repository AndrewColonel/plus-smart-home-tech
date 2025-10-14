package ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Condition;

@Slf4j
@Component
public class TemperatureProcessorHandler extends BaseSnapshotProcessorHandler implements SnapshotProcessorHandler {

    @Override
    public String getRecordType() {
        return "TemperatureSensorAvro";
    }

    @Override
    public boolean handleScenario(SensorStateAvro sensorState, Condition condition,
                                  String idSensor, String scenarioName) {
        log.info("Начинаю регулеровку по датчикам температуры");
        boolean conditionState = false;
        if (sensorState.getData() instanceof TemperatureSensorAvro sensor) {
            conditionState = conditionOperationCheck(sensor.getTemperatureC(), condition,
                    idSensor, scenarioName);
        }

        return conditionState;
    }
}
