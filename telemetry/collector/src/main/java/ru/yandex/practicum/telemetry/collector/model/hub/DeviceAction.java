package ru.yandex.practicum.telemetry.collector.model.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.model.ActionType;

@Getter
@Setter
@ToString
public class DeviceAction {
    // Представляет действие, которое должно быть выполнено устройством
    private String sensorId;
    private ActionType type;
    private int value;
}
