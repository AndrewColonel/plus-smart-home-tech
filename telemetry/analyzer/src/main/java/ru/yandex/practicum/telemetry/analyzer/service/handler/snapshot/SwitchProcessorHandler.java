package ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Condition;

@Slf4j
@Component
public class SwitchProcessorHandler extends BaseSnapshotProcessorHandler implements SnapshotProcessorHandler {

    @Override
    public String getRecordType() {
        return "SwitchSensorAvro";
    }

    @Override
    public boolean handleScenario(SensorStateAvro sensorState, Condition condition,
                                  String idSensor, String scenarioName) {
        log.info("Начинаю регулеровку выключателей");
        boolean conditionState = false;
        if (sensorState.getData() instanceof SwitchSensorAvro sensor) {
            conditionState = conditionOperationCheck((sensor.getState() ? 1 : 0), condition,
                    idSensor, scenarioName);
        }

        return conditionState;

    }
}
