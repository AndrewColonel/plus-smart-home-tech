package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConfCons2;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConfig;
import ru.yandex.practicum.telemetry.analyzer.service.handler.HubProcessorHandler;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HubEventProcessor extends BaseProcessor {

    private final Map<String, HubProcessorHandler> hubProcessorHandlers;

//    private final KafkaConfig.ConsumerConfig consumerConfig;

    @Autowired
    public HubEventProcessor(KafkaConfig kafkaConfig,
                             List<HubProcessorHandler> hubProcessorHandlers) {
        super(kafkaConfig.getHubConsumer().getProperties(),
                kafkaConfig.getHubConsumer().getTopic(),
                kafkaConfig.getHubConsumer().getPollTimeout());

        this.hubProcessorHandlers = hubProcessorHandlers.stream()
                .collect(Collectors.toMap(HubProcessorHandler::getRecordType,
                        Function.identity()));
    }


//
//    public HubEventProcessor(KafkaConfCons2 cofiguration,
//                             List<HubProcessorHandler> hubProcessorHandlers,
//                             KafkaConfig kafkaConfig) {
//        super(cofiguration.getConsumerConfig(),
//                cofiguration.getTelemetryHubTopic(),
//                Duration.ofMillis(100));
//
//        this.consumerConfig = kafkaConfig.getHubConsumer();
//
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        System.out.println(consumerConfig.getProperties());
//
//        this.hubProcessorHandlers = hubProcessorHandlers.stream()
//                .collect(Collectors.toMap(HubProcessorHandler::getRecordType,
//                        Function.identity()));
//    }

//    public HubEventProcessor(KafkaConfCons2 cofiguration,
//                             List<HubProcessorHandler> hubProcessorHandlers) {
//        super(new KafkaConsumer<String, SpecificRecordBase>(cofiguration.getConsumerConfig())
//                ,cofiguration.getTelemetryHubTopic());
//
//        this.hubProcessorHandlers = hubProcessorHandlers.stream()
//                .collect(Collectors.toMap(HubProcessorHandler::getRecordType,
//                        Function.identity()));
//    }
//


    @Override
    public void handleRecord(ConsumerRecord<String, SpecificRecordBase> record) {
        log.debug("<<< Получено сообщение топика = {}, партиция = {}, смещение = {}, значение: {}\n",
                record.topic(), record.partition(), record.offset(), record.value());
        log.info(">>> Сообщение хаба: <<< {}", record.value());
        if (record.value() instanceof HubEventAvro event) {
            String handlerName = event.getPayload().getClass().getSimpleName();
            HubProcessorHandler handler = hubProcessorHandlers.get(handlerName);
            if (Objects.nonNull(handler)) {
                log.debug("Выбран обработчик {}",handler.getClass().getSimpleName());
                handler.handleRecord(event);
            } else {
                log.debug("Обработчика для {} не найдено",handlerName);
            }
        }
    }
}