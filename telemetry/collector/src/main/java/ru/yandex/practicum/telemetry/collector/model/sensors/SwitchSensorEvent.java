package ru.yandex.practicum.telemetry.collector.model.sensors;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.model.SensorEventType;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString(callSuper = true)
public class SwitchSensorEvent extends SensorEvent {
    // Событие датчика переключателя, содержащее информацию о текущем состоянии переключателя
    @NotBlank
    private Boolean state;
    @NotBlank
    private SensorEventType type;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }

}
