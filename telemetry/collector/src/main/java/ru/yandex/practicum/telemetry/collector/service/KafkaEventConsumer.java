package ru.yandex.practicum.telemetry.collector.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.VoidDeserializer;

import ru.yandex.practicum.kafka.serializer.DeviceAddedEventAvroDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;


import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Slf4j
public class KafkaEventConsumer {


    public static void main(String[] args) {
        Properties config = new Properties();
        // эти настройки нужны, чтобы консьюмер всегда читал сообщения с самого начала топика (то есть все сообщения)
        config.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // обязательные настройки
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DeviceAddedEventAvroDeserializer.class);


        String topic = "telemetry.hubs.v1";

        try (Consumer<String, DeviceAddedEventAvro> consumer = new KafkaConsumer<>(config)) {
            consumer.subscribe(List.of(TelemetryTopics.TELEMETRY_HUBS_TOPIC,
                    TelemetryTopics.TELEMETRY_SENSORS_TOPIC));

            log.info("Начинаю приём сообщений из топика: {}", topic);

            while (true) {
                ConsumerRecords<String, DeviceAddedEventAvro> records =
                        consumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, DeviceAddedEventAvro> record : records) {
                    log.info("Получено сообщение из партиции {}, со смещением {}:\n{}\n",
                            record.partition(), record.offset(), record.value());
                }
            }
        }
    }
}