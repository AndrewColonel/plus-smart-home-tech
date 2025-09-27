package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.common.TelemetryTopics;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.sensors.SensorEvent;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

import java.util.concurrent.Future;

@AllArgsConstructor
@Slf4j
public abstract class BaseSensorEventHandler<T> {

    private final KafkaEventProducer producer;

    public abstract T toAvro(SensorEvent event);

    public void handle(SensorEvent event) {
        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(toAvro(event))
                .build();
        Producer<String, SpecificRecordBase> producer = this.producer.getProducer();
        String topic = TelemetryTopics.TELEMETRY_SENSORS_TOPIC;
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, sensorEventAvro);
        log.info(String.format("Объект Avro для отправки в брокер %s в топик %s", sensorEventAvro, topic));

        Future<RecordMetadata> metadataFuture = producer.send(record);
        log.info("Состояние отправки: {} ", metadataFuture.isDone());
        producer.flush();
        log.info("Состояние отправки: {} ", metadataFuture.isDone());
    }
}
