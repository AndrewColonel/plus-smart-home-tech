package ru.yandex.practicum.telemetry.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConfig;
import ru.yandex.practicum.telemetry.analyzer.service.handler.snapshot.SnapshotDeviceActionImpl;

@Slf4j
@Component
public class SnapshotProcessor extends BaseProcessor {

    private final SnapshotDeviceActionImpl snapshotDeviceAction;

    @Autowired
    public SnapshotProcessor(KafkaConfig kafkaConfig, SnapshotDeviceActionImpl snapshotDeviceAction) {
        super(kafkaConfig.getSnapshotConsumer().getProperties(),
                kafkaConfig.getSnapshotConsumer().getTopic(),
                kafkaConfig.getSnapshotConsumer().getPollTimeout());
        this.snapshotDeviceAction = snapshotDeviceAction;
    }

    @Override
    public void handleRecord(ConsumerRecord<String, SpecificRecordBase> record) {
        log.debug("<<< Получено сообщение топика = {}, партиция = {}, смещение = {}, значение: {}\n",
                record.topic(), record.partition(), record.offset(), record.value());
        log.info("+++ Получен снапшот: +++ {}", record.value());
        if (record.value() instanceof SensorsSnapshotAvro event) {
            snapshotDeviceAction.handleScenario(event);
        }
    }
}
