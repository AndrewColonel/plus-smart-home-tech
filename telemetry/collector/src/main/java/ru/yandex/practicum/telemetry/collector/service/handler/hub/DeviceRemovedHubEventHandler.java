package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.model.DeviceRemovedEvent;
import ru.yandex.practicum.telemetry.collector.model.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.HubEventType;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.service.handler.HubEventHandler;

@Component
@AllArgsConstructor
@Slf4j
public class DeviceRemovedHubEventHandler implements HubEventHandler {

    private final KafkaEventProducer kafkaEventProducer;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }

    private DeviceRemovedEventAvro toAvro(HubEvent event) {
        DeviceRemovedEvent _event = (DeviceRemovedEvent) event;
        return DeviceRemovedEventAvro.newBuilder()
                .setId(_event.getId())
                .build();
    }

    @Override
    public void handle(HubEvent event) {
        String topic = "telemetry.hubs.v1";
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, toAvro(event));
        log.info(String.format("Создана запись для Avro %s",toAvro(event)));
        kafkaEventProducer.getProducer().send(record);
//        kafkaEventProducer.getProducer().close();
        kafkaEventProducer.getProducer().flush();
    }
}
