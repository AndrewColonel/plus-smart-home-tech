package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.telemetry.collector.service.KafkaClient;
import ru.yandex.practicum.telemetry.collector.service.handler.SensorEventHandler;

@Component
public class ClimateSensorEventHandler extends BaseSensorEventHandler<ClimateSensorAvro> implements SensorEventHandler {

    public ClimateSensorEventHandler(KafkaClient producer) {
        super(producer);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public ClimateSensorAvro toAvro(SensorEventProto event) {
        ClimateSensorProto _event = event.getClimateSensorEvent();
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(+_event.getTemperatureC())
                .setHumidity(_event.getHumidity())
                .setCo2Level(_event.getCo2Level())
                .build();
    }
}
