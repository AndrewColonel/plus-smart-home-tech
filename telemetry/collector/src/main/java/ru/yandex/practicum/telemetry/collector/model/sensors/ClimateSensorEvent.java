package ru.yandex.practicum.telemetry.collector.model.sensors;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.model.SensorEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class ClimateSensorEvent extends SensorEvent {
    // Событие климатического датчика, содержащее информацию о температуре, влажности и уровне CO2
    @NotBlank
    private int temperatureC;
    @NotBlank
    private int humidity;
    @NotBlank
    private int co2Level;
    @NotBlank
    private SensorEventType type;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

}
