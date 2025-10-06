package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.telemetry.collector.service.KafkaClient;
import ru.yandex.practicum.telemetry.collector.service.handler.SensorEventHandler;

@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro> implements SensorEventHandler {

    public SwitchSensorEventHandler(KafkaClient producer) {
        super(producer);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }

    @Override
    public SwitchSensorAvro toAvro(SensorEventProto event) {
        SwitchSensorProto _event = event.getSwitchSensorEvent();
        return SwitchSensorAvro.newBuilder()
                .setState(_event.getState())
                .build();
    }
}
