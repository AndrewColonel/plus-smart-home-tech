package ru.yandex.practicum.telemetry.collector.service;

import lombok.Getter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.serializer.GeneralAvroSerializer;

import java.util.Objects;
import java.util.Properties;

@Component
@Getter
public class KafkaEventProducer {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootStrapServers;
    @Value(value = "${spring.kafka.topics.telemetry-hubs-topics}")
    private String telemetryHubTopic;
    @Value(value = "${spring.kafka.topics.telemetry-sensors-topics}")
    private String telemetrySensorTopic;

    private Producer<String, SpecificRecordBase> producer;

    private void initProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootStrapServers);
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

}
