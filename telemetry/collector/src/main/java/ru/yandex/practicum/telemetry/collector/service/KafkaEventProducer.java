package ru.yandex.practicum.telemetry.collector.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.collector.config.KafkaConfig;

import java.util.Objects;

@Component
public class KafkaEventProducer {

    private final KafkaConfig.ProducerConfig producerConfig;
    private Producer<String, SpecificRecordBase> producer;

    @Autowired
    public KafkaEventProducer(KafkaConfig kafkaConfig) {
        this.producerConfig = kafkaConfig.getProducerConfig();
    }

    public Producer<String, SpecificRecordBase> getProducer() {
        if (Objects.isNull(producer)) {
            producer = new KafkaProducer<>(producerConfig.getProperties());
        }
        return producer;
    }

    public String getSensorTopic() {
        return producerConfig.getSensortopic();
    }
    public String getHubTopic() {
        return producerConfig.getHubtopic();
    }

    public void closeProducer() {
        if (Objects.nonNull(producer)) {
            producer.close();
        }
    }

}
