package ru.yandex.practicum.telemetry.collector.service.handler;

import ru.yandex.practicum.telemetry.collector.model.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.sensors.SensorEvent;

public interface SensorEventHandler {

    HubEventType getMessageType();

    void handle(SensorEvent event);

}
