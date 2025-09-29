package ru.yandex.practicum.telemetry.collector.model.sensors;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.model.SensorEventType;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString(callSuper = true)
public class TemperatureSensorEvent extends SensorEvent {
    @NotBlank
    private int temperatureC;
    @NotBlank
    private int temperatureF;
    @NotBlank
    private SensorEventType type;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }

}
