package ru.yandex.practicum.telemetry.collector.model.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.model.ConditionOperation;
import ru.yandex.practicum.telemetry.collector.model.ConditionType;

@Getter
@Setter
@ToString
public class ScenarioCondition {
    // Условие сценария, которое содержит информацию о датчике, типе условия, операции и значении
    private String sensorId;
    private ConditionType type;
    private ConditionOperation operation;
    private int value;
}
