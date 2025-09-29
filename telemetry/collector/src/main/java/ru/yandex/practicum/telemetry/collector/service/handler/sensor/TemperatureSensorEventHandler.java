package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.telemetry.collector.model.SensorEventType;
import ru.yandex.practicum.telemetry.collector.model.sensors.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensors.TemperatureSensorEvent;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.service.handler.SensorEventHandler;

@Component
public class TemperatureSensorEventHandler extends  BaseSensorEventHandler<TemperatureSensorAvro> implements SensorEventHandler {

    public TemperatureSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public TemperatureSensorAvro toAvro(SensorEvent event) {
        TemperatureSensorEvent _event = (TemperatureSensorEvent) event;
        return TemperatureSensorAvro.newBuilder()
                .setId(_event.getId())
                .setHubId(_event.getHubId())
                .setTimestamp(_event.getTimestamp())
                .setTemperatureC(_event.getTemperatureC())
                .setTemperatureF(_event.getTemperatureF())
                .build();
    }
}
