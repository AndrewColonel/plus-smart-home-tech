package ru.yandex.practicum.telemetry.collector.service.handler;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface SensorEventHandler {

    SensorEventProto.PayloadCase getMessageType();

    SensorEventAvro handle(SensorEventProto event);

}
