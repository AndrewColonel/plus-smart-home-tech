package ru.yandex.practicum.telemetry.aggregator.kafkaclient;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class KafkaClient {

    private final KafkaClientConfiguration cofiguration;

    private Producer<String, SpecificRecordBase> producer;
    private Consumer<String, SpecificRecordBase> consumer;

    public Producer<String, SpecificRecordBase> getProducer() {
        if (Objects.isNull(producer)) {
            producer = new KafkaProducer<>(cofiguration.getProduserConfig());
        }
        return producer;
    }

    public Consumer<String, SpecificRecordBase> getConsumer() {
        if (Objects.isNull(consumer)) {
            consumer = new KafkaConsumer<>(cofiguration.getConsumerConfig());
        }
        return consumer;
    }

    public String getTelemetrySensorTopic() {
        return cofiguration.getTelemetrySensorTopic();
    }

    public String getTelemetrySnapshotsTopic() {
        return cofiguration.getTelemetrySnapshotsTopic();
    }

}
