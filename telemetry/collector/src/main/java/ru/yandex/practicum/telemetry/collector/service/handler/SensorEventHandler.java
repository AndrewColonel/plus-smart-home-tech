package ru.yandex.practicum.telemetry.collector.service.handler;

import ru.yandex.practicum.telemetry.collector.model.SensorEventType;
import ru.yandex.practicum.telemetry.collector.model.sensors.SensorEvent;

public interface SensorEventHandler {

    SensorEventType getMessageType();

    void handle(SensorEvent event);

}
