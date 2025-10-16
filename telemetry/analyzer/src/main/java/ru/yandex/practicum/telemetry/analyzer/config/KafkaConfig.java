package ru.yandex.practicum.telemetry.analyzer.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Properties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "analyzer.kafka")
public class KafkaConfig {

    private final ConsumerConfig hubConsumer;
    private final ConsumerConfig snapshotConsumer;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ConsumerConfig {
        private Properties properties;
        private String topic;
        private Duration pollTimeout;
    }
}
