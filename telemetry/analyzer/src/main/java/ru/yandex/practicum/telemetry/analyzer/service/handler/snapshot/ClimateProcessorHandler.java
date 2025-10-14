package ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Condition;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.ConditionType;

@Slf4j
@Component
public class ClimateProcessorHandler extends BaseSnapshotProcessorHandler  implements SnapshotProcessorHandler
{


    @Override
    public String getRecordType() {
        return "ClimateSensorAvro";
    }

    @Override
    public boolean handleScenario(SensorStateAvro sensorState, Condition condition,
                                  String idSensor, String scenarioName) {
        log.info("Начинаю проверку по датчикам климата");
        boolean conditionState = false;
        if (sensorState.getData() instanceof ClimateSensorAvro sensor) {
            switch (ConditionType.valueOf(condition.getType())) {
                case ConditionType.TEMPERATURE -> {
                    conditionState = conditionOperationCheck(sensor.getTemperatureC(), condition,
                            idSensor, scenarioName);
                }
                case ConditionType.CO2LEVEL -> {
                    conditionState = conditionOperationCheck(sensor.getCo2Level(), condition,
                            idSensor, scenarioName);
                }
                case ConditionType.HUMIDITY -> {
                    conditionState = conditionOperationCheck(sensor.getHumidity(), condition,
                            idSensor, scenarioName);
                }

            }
        }

        return conditionState;

    }
}
