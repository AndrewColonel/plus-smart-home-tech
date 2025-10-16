package ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Condition;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.ConditionOperation;


@Slf4j
public class BaseSnapshotProcessorHandler {

    public boolean conditionOperationCheck(int sensorValue, Condition condition,
                                           String idSensor, String scenarioName) {
        boolean conditionState = false;
        switch (ConditionOperation.valueOf(condition.getOperation())) {

            case ConditionOperation.EQUALS -> {
                conditionState = Integer.valueOf(sensorValue).equals(condition.getValue());
            }
            case ConditionOperation.GREATER_THAN -> {
                conditionState = (sensorValue) > (condition.getValue());
            }
            case ConditionOperation.LOWER_THAN -> {
                conditionState = (sensorValue) < (condition.getValue());
            }
        }
        log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        log.debug("Проверка кондиции");
        log.debug("сенсор: {} типа: {}", idSensor, condition.getType());
        log.info("кондиция {} сценария {}", condition.getId(), scenarioName);
        log.debug("пороговое значение: {}", condition.getValue());
        log.debug("условие: {}", condition.getOperation());
        log.debug("изменение: {}", sensorValue);
        log.info("выполниться: {}", conditionState);
        log.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        return conditionState;
    }

}
