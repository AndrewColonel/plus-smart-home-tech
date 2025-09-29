package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

import java.util.concurrent.Future;

@Slf4j
@AllArgsConstructor
public abstract class BaseHubEventHandler<T> {

    private final KafkaEventProducer producer;

    public abstract T toAvro(HubEvent event);

    public void handle(HubEvent event) {
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(toAvro(event))
                .build();
        Producer<String, SpecificRecordBase> producer = this.producer.getProducer();
        String topic = this.producer.getTelemetryHubTopic();
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, eventAvro);
        log.info(String.format("Объект Avro для отправки в брокер %s в топик %s", eventAvro, topic));

        Future<RecordMetadata> metadataFuture = producer.send(record);
        log.info("Состояние отправки: {} ", metadataFuture.isDone());
        producer.flush();
        log.info("Состояние отправки: {} ", metadataFuture.isDone());
    }
}
