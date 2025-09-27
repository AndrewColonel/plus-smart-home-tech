package ru.yandex.practicum.telemetry.collector.common;

import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.telemetry.collector.model.ActionType;
import ru.yandex.practicum.telemetry.collector.model.ConditionOperation;
import ru.yandex.practicum.telemetry.collector.model.ConditionType;
import ru.yandex.practicum.telemetry.collector.model.DeviceType;

import java.util.EnumMap;

public class EnumMapper {


    public static ConditionOperationAvro toConditionOperationAvro(ConditionOperation operation) {
    }

    public static ConditionTypeAvro toConditionTypeAvro(ConditionType type) {
    }


    public static ActionTypeAvro toActionTypeAvro(ActionType type) {
    }

    public static EnumMap<DeviceType, DeviceTypeAvro> map;
    static {
        map = new EnumMap<>(DeviceType.class);
        map.put(DeviceType.MOTION_SENSOR, DeviceTypeAvro.MOTION_SENSOR);
        map.put(DeviceType.TEMPERATURE_SENSOR, DeviceTypeAvro.TEMPERATURE_SENSOR);
        map.put(DeviceType.LIGHT_SENSOR, DeviceTypeAvro.LIGHT_SENSOR);
        map.put(DeviceType.CLIMATE_SENSOR, DeviceTypeAvro.CLIMATE_SENSOR);
        map.put(DeviceType.SWITCH_SENSOR, DeviceTypeAvro.SWITCH_SENSOR);
    }

}
