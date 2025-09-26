package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;

import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

import ru.yandex.practicum.telemetry.collector.model.hub.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.DeviceType;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.HubEventType;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

import ru.yandex.practicum.telemetry.collector.service.handler.HubEventHandler;

import java.util.EnumMap;

@Component
public class DeviceAddedHubEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> implements HubEventHandler{

    public DeviceAddedHubEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_ADDED;
    }

    @Override
    public DeviceAddedEventAvro toAvro(HubEvent event) {
        DeviceAddedEvent _event = (DeviceAddedEvent) event;
        return DeviceAddedEventAvro.newBuilder()
                .setId(_event.getId())
                .setType(map.get(_event.getDeviceType()))
                .build();
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
