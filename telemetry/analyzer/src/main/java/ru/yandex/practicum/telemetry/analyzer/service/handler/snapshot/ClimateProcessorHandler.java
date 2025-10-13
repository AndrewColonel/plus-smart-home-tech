package ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.ConditionType;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Scenario;

import java.util.List;
import java.util.Optional;


@Slf4j
@Component

public class ClimateProcessorHandler extends BaseSnapshotProcessorHandler implements SnapshotProcessorHandler {


    @Override
    public String getRecordType() {
        return "ClimateSensorAvro";
    }

    @Override
    public Optional<List<Scenario>> handleScenario(SensorsSnapshotAvro event) {
        log.info("Начинаю проверку по датчикам климата");
//        SensorStateAvro sensorState = event.getSensorsState().get(idSensor);
//        boolean conditionState = false;
//
//        if (sensorState.getData() instanceof ClimateSensorAvro sensor) {
//            switch (ConditionType.valueOf(condition.getType())) {
//                case ConditionType.TEMPERATURE -> {
//                    conditionState = conditionOperationCheck(sensor.getTemperatureC(), condition,
//                            checkScenario.getName());
//                }
//                case ConditionType.CO2LEVEL -> {
//                    conditionState = conditionOperationCheck(sensor.getCo2Level(), condition,
//                            checkScenario.getName());
//                }
//                case ConditionType.HUMIDITY -> {
//                    conditionState = conditionOperationCheck(sensor.getHumidity(), condition,
//                            checkScenario.getName());
//                }
//
//            }
//        }


        return Optional.empty();

    }
}
