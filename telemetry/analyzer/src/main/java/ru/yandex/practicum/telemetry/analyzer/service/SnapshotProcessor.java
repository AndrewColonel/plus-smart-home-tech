package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConfig;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConfiguration;
import ru.yandex.practicum.telemetry.analyzer.service.handler.SnapshotProcessorHandler;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SnapshotProcessor extends BaseProcessor {

    private final Map<String, SnapshotProcessorHandler> snapshotProcessorHandlers;

//    private final KafkaConfig.ConsumerConfig consumerConfig;

    @Autowired
    public SnapshotProcessor(KafkaConfig kafkaConfig,
                             List<SnapshotProcessorHandler> snapshotProcessorHandlers) {
        super(kafkaConfig.getSnapshotConsumer().getProperties(),
                kafkaConfig.getSnapshotConsumer().getTopic(),
                kafkaConfig.getSnapshotConsumer().getPollTimeout());

        this.snapshotProcessorHandlers = snapshotProcessorHandlers.stream()
                .collect(Collectors.toMap(SnapshotProcessorHandler::getRecordType,
                        Function.identity()));
    }



//    public SnapshotProcessor(KafkaConfiguration cofiguration,
//                             List<SnapshotProcessorHandler> snapshotProcessorHandlers,
//                             KafkaConfig kafkaConfig) {
//        super(cofiguration.getConsumerConfig(),
//                cofiguration.getTelemetryHubTopic(),
//                Duration.ofMillis(100));
//
//
//        this.consumerConfig = kafkaConfig.getSnapshotConsumer();
//
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        System.out.println(consumerConfig.getProperties());
//
//
//
//        this.snapshotProcessorHandlers = snapshotProcessorHandlers.stream()
//                .collect(Collectors.toMap(SnapshotProcessorHandler::getRecordType,
//                        Function.identity()));
//    }



//
//    public SnapshotProcessor(KafkaConfiguration cofiguration,
//                             List<SnapshotProcessorHandler> snapshotProcessorHandlers) {
//        super(new KafkaConsumer<String, SpecificRecordBase>(cofiguration.getConsumerConfig())
//                ,cofiguration.getTelemetrySnapshotsTopic());
//
//        this.snapshotProcessorHandlers = snapshotProcessorHandlers.stream()
//                .collect(Collectors.toMap(SnapshotProcessorHandler::getRecordType,
//                        Function.identity()));
//    }


    @Override
    public void handleRecord(ConsumerRecord<String, SpecificRecordBase> record) {
        log.debug("<<< Получено сообщение топика = {}, партиция = {}, смещение = {}, значение: {}\n",
                record.topic(), record.partition(), record.offset(), record.value());
        log.info("+++ Получен снапшот: +++ {}", record.value());
        if (record.value() instanceof SensorsSnapshotAvro event) {



            String handlerName = event.getSensorsState().getClass().getSimpleName();
            SnapshotProcessorHandler handler = snapshotProcessorHandlers.get(handlerName);




            if (Objects.nonNull(handler)) {
                log.debug("Выбран обработчик {}",handler.getClass().getSimpleName());
                handler.handleRecord(event);
            } else {
                log.debug("Обработчика для {} не найдено",handlerName);
            }
        }
    }


}
