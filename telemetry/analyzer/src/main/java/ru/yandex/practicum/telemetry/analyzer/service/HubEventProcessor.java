package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConfiguration;
import ru.yandex.practicum.telemetry.analyzer.service.handler.HubProcessorHandler;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {

    private final KafkaConfiguration cofiguration;

    private final Consumer<String, SpecificRecordBase> consumer;

    private final Map<String, HubProcessorHandler> hubProcessorHandlers;

    @Autowired
    public HubEventProcessor(KafkaConfiguration cofiguration, List<HubProcessorHandler> hubProcessorHandlers) {
        this.cofiguration = cofiguration;
        this.consumer = new KafkaConsumer<>(cofiguration.getConsumerConfig());

        this.hubProcessorHandlers = hubProcessorHandlers.stream()
                .collect(Collectors.toMap(HubProcessorHandler::getRecordType,
                        Function.identity()));

    }

    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private static final Duration CONSUME_ATTEMPT_TIMEOUT = Duration.ofMillis(1000);

    @Override
    public void run() {
        String topicConsumer = cofiguration.getTelemetryHubTopic();
        Runtime.getRuntime().addShutdownHook(new Thread(new Thread(() -> {
            log.info("Сработал хук на завершение JVM. Прерываем работу консьюмера");
            consumer.wakeup();
        })));

        try {
            consumer.subscribe(List.of(topicConsumer));
            log.info("Начинаю приём сообщений из топика: {}", topicConsumer);

            while (true) {

                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(CONSUME_ATTEMPT_TIMEOUT);
                int count = 0;
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {

                    // TODO
                    handleRecord(record);

                    manageOffsets(record, count, consumer);
                    count++;
                }
                consumer.commitAsync();
            }

        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                consumer.commitSync(currentOffsets);
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
            }
        }
    }

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

    private void handleRecord(ConsumerRecord<String, SpecificRecordBase> record) {
        log.debug("<<< Получено сообщение топика = {}, партиция = {}, смещение = {}, значение: {}\n",
                record.topic(), record.partition(), record.offset(), record.value());
        log.info(">>> Сообщение хаба: <<< {}", record.value());
        if (record.value() instanceof HubEventAvro event) {
            String handlerName = event.getPayload().getClass().getSimpleName();
            HubProcessorHandler handler = hubProcessorHandlers.get(handlerName);
            if (Objects.nonNull(handler)) {
                log.trace("Выбран обработчик {}",handler.getClass().getSimpleName());
                handler.handleRecord(event);
            } else {
                log.trace("Обработчика для {} не найдено",handlerName);
            }
        }
    }
}