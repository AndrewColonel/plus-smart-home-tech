package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

import java.time.Instant;
import java.util.concurrent.Future;

@Slf4j
@AllArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> {

    private final KafkaEventProducer kafkaEventProducer;

    public abstract T toAvro(HubEventProto event);

    public void handle(HubEventProto event) {
        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(),
                        event.getTimestamp().getNanos()))
                .setPayload(toAvro(event))
                .build();
        Producer<String, SpecificRecordBase> producer = kafkaEventProducer.getProducer();
        String topic = kafkaEventProducer.getHubTopic();
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, hubEventAvro);
        log.info("Объект Avro для отправки в брокер {} в топик {}", hubEventAvro, topic);

        Future<RecordMetadata> metadataFuture = producer.send(record);
        log.info("Состояние отправки: {} ", metadataFuture.isDone());
        producer.flush();
        log.info("Состояние отправки: {} ", metadataFuture.isDone());
    }
}
