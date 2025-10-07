package ru.yandex.practicum.telemetry.collector.service;

import lombok.Getter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.serializer.GeneralAvroSerializer;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Getter
public class KafkaClient {

    private final AtomicInteger counter = new AtomicInteger(0);

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootStrapServers;
    @Value(value = "${spring.kafka.topics.telemetry-hubs-topics}")
    private String telemetryHubTopic;
    @Value(value = "${spring.kafka.topics.telemetry-sensors-topics}")
    private String telemetrySensorTopic;
    @Value(value = "${spring.kafka.topics.telemetry-snapshots-topics}")
    private String telemetrySnapshotsTopic;

    @Value(value = "${spring.kafka.consumer.client-id}")
    private String clientId;
    @Value(value = "${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value(value = "${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;
    @Value(value = "${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;
    @Value(value = "${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializer;
    @Value(value = "${spring.kafka.consumer.max-poll-record-config}")
    private String maxPollRecordConfig;
    @Value(value = "${spring.kafka.consumer.fetch-max-bytes-config}")
    private String fetchMaxBytesConfig;
    @Value(value = "${spring.kafka.consumer.max-partition-fetch-bytes-config}")
    private String maxPartitionFetchBytesConfig;

    private Producer<String, SpecificRecordBase> producer;

    private Consumer<String, SpecificRecordBase> consumer;

    private void initProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);

        producer = new KafkaProducer<>(config);
    }

    public Producer<String, SpecificRecordBase> getProducer() {
        if (Objects.isNull(producer)) {
            initProducer();
        }
        return producer;
    }

    public Properties getConsumerProperties() {
        Properties config = new Properties();

        config.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId + counter.getAndIncrement());

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);

//        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecordConfig);
//        config.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, fetchMaxBytesConfig);
//        config.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytesConfig);

        return config;
    }

    public Consumer<String, SpecificRecordBase> getConsumer() {
        if (Objects.isNull(consumer)) {
            consumer = new KafkaConsumer<>(getConsumerProperties());
        }
        return consumer;
    }

    public Consumer<String, SpecificRecordBase> getConsumer(Properties config) {
        if (Objects.isNull(consumer)) {
            consumer = new KafkaConsumer<>(config);
        }
        return consumer;
    }

}
