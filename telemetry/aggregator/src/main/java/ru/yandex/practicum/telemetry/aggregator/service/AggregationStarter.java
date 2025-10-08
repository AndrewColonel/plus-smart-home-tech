package ru.yandex.practicum.telemetry.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.deserializer.SensorEventDeserializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;


import java.time.Duration;
import java.util.*;


/**
 * Класс AggregationStarter, ответственный за запуск агрегации данных.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    private final KafkaClient kafkaClient;

    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);

    private final SnapshotService snapshotService;

    /**
     * Метод для начала процесса агрегации данных.
     * Подписывается на топики для получения событий от датчиков,
     * формирует снимок их состояния и записывает в кафку.
     */
    public void start() {
        // готовим консьюмер для получение данных SensorEventAvro из топика telemetry.sensors.v1
        Properties config = kafkaClient.getConsumerProperties();
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorEventDeserializer.class);
        Consumer<String, SpecificRecordBase> consumer = kafkaClient.getConsumer(config);
        String topicConsumer = kafkaClient.getTelemetrySensorTopic();

        // готовим продьюсер для отправки подготовленных снапшотов SensorsSnapshotAvro в топик telemetry.snapshots.v1
        Producer<String, SpecificRecordBase> producer = kafkaClient.getProducer();
        String topicProducer = kafkaClient.getTelemetrySnapshotsTopic();

        // регистрируем хук, в котором вызываем метод wakeup.
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

        try {
            //подготовка к обработке данных, подписка на топик ...
            consumer.subscribe(List.of(topicConsumer));
            log.info("Начинаю приём сообщений из топика: {}", topicConsumer);
            // Цикл обработки событий
            while (true) {
                // реализация цикла опроса и обработка полученных данных
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);

                int count = 0;
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    // обрабатываем очередную запись
                    handleRecord(record, producer, topicProducer);
                    // фиксируем оффсеты обработанных записей, каждые 10 штук
                    manageOffsets(record, count, consumer);
                    count++;
                }
                // фиксируем максимальный оффсет обработанных записей
                consumer.commitAsync();
//                snapshots.clear();
            }
        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                // Перед тем, как закрыть продюсер и консьюмер, нужно убедится,
                // что все сообщения, лежащие в буффере, отправлены и
                // все оффсеты обработанных сообщений зафиксированы
                // здесь нужно вызвать метод продюсера для сброса данных в буффере
                producer.flush();
                // здесь нужно вызвать метод консьюмера для фиксиции смещений
                consumer.commitSync(currentOffsets);
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }

    // вспомогательны метод для фиксации промежутоного оффсета (каждые 10 записей)
    private void manageOffsets(ConsumerRecord<String, SpecificRecordBase> record, int count,
                               Consumer<String, SpecificRecordBase> consumer) {
        // обновляем текущий оффсет для топика-партиции
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );
        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (Objects.nonNull(exception)) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });
        }
    }

    // подготовка и отправка снапшота
    private void handleRecord(
            ConsumerRecord<String, SpecificRecordBase> record,
            Producer<String, SpecificRecordBase> producer,
            String topicProducer) throws InterruptedException {
        log.info("<<< Получено сообщение топика = {}, партиция = {}, смещение = {}, значение: {}\n",
                record.topic(), record.partition(), record.offset(), record.value());
        if (record.value() instanceof SensorEventAvro event) {
            Optional<SensorsSnapshotAvro> optionalSensorsSnapshotAvro = snapshotService.updateState(event);
            // если снапшот сформирован, то его надо отправить в брокер
            if (optionalSensorsSnapshotAvro.isPresent()) {
                ProducerRecord<String, SpecificRecordBase> producerRecord =
                        new ProducerRecord<>(topicProducer, optionalSensorsSnapshotAvro.get());
                log.info(">>> Снапшот {} для отправки в топик {}", optionalSensorsSnapshotAvro.get(), topicProducer);
                producer.send(producerRecord);
            } else {
                log.info("<--- Изменений в состоянии телеметрии нет --->");
            }
        }
    }

}