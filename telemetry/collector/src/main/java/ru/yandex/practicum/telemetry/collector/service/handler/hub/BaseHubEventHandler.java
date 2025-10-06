package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.service.KafkaClient;

import java.time.Instant;
import java.util.concurrent.Future;

@Slf4j
@AllArgsConstructor
public abstract class BaseHubEventHandler<T> {

    private final KafkaClient kafkaClient;

    public abstract T toAvro(HubEventProto event);

    public void handle(HubEventProto event) {
        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(),
                        event.getTimestamp().getNanos()))
                .setPayload(toAvro(event))
                .build();
        Producer<String, SpecificRecordBase> producer = kafkaClient.getProducer();
        String topic = kafkaClient.getTelemetryHubTopic();
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, eventAvro);
        log.info(String.format("Объект Avro для отправки в брокер %s в топик %s", eventAvro, topic));

        Future<RecordMetadata> metadataFuture = producer.send(record);
        log.info("Состояние отправки: {} ", metadataFuture.isDone());
        producer.flush();
        log.info("Состояние отправки: {} ", metadataFuture.isDone());
    }
}
