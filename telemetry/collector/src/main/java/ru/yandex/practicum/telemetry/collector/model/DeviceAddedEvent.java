package ru.yandex.practicum.telemetry.collector.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString(callSuper = true)
public class DeviceAddedEvent extends HubEvent {
    // Событие, сигнализирующее о добавлении нового устройства в систему
    @NotBlank
    private String id;
    @NotBlank
    private DeviceType deviceType;
    @NotBlank
    private HubEventType type;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }

}
