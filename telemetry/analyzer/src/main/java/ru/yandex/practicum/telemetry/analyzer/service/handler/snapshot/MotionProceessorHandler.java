package ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Condition;

@Slf4j
@Component
public class MotionProceessorHandler extends BaseSnapshotProcessorHandler implements SnapshotProcessorHandler {

    @Override
    public String getRecordType() {
        return "MotionSensorAvro";
    }

    @Override
    public boolean handleScenario(SensorStateAvro sensorState, Condition condition,
                                  String idSensor, String scenarioName) {
        log.info("Начинаю регулеровку по датчикам движения");
        boolean conditionState = false;

        if (sensorState.getData() instanceof MotionSensorAvro sensor) {
            conditionState = conditionOperationCheck((sensor.getMotion() ? 1 : 0), condition,
                    idSensor, scenarioName);
        }
        return conditionState;
    }
}
