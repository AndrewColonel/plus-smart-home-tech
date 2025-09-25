package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.telemetry.collector.model.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.DeviceType;
import ru.yandex.practicum.telemetry.collector.model.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.HubEventType;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.service.handler.HubEventHandler;

import java.util.EnumMap;
import java.util.concurrent.Future;


@Component
@AllArgsConstructor
@Slf4j
public class DeviceAddedHubEventHandler implements HubEventHandler {

    private final KafkaEventProducer kafkaEventProducer;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_ADDED;
    }

    private DeviceAddedEventAvro toAvro(HubEvent event) {
        DeviceAddedEvent _event = (DeviceAddedEvent) event;
        return DeviceAddedEventAvro.newBuilder()
                .setId(_event.getId())
                .setType(map.get(_event.getDeviceType()))
                .build();
    }


    @Override
    public void handle(HubEvent event) {
        Producer<String, SpecificRecordBase> producer = kafkaEventProducer.getProducer();
        String topic = "telemetry.hubs.v1";
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, toAvro(event));
        log.info(String.format("Создана запись для Avro %s", toAvro(event)));
//        kafkaEventProducer.getProducer().send(record);
//        kafkaEventProducer.getProducer().flush();
//        kafkaEventProducer.getProducer().close();

//        try (Producer<String, SpecificRecordBase> producer = kafkaEventProducer.initProducer1()) {
//            producer.send(record);
//            producer.flush();
//
//        }
//        Producer<String, SpecificRecordBase> producer = kafkaEventProducer.getProducer();
        Future<RecordMetadata> metadataFuture = producer.send(record);
        log.info("Состояние отправки: {} ", metadataFuture.isDone());
        producer.flush();
        log.info("Состояние отправки: {} ", metadataFuture.isDone());


    }

    public static EnumMap<DeviceType, DeviceTypeAvro> map;

    static {
        map = new EnumMap<>(DeviceType.class);
        map.put(DeviceType.MOTION_SENSOR, DeviceTypeAvro.MOTION_SENSOR);
        map.put(DeviceType.TEMPERATURE_SENSOR, DeviceTypeAvro.TEMPERATURE_SENSOR);
        map.put(DeviceType.LIGHT_SENSOR, DeviceTypeAvro.LIGHT_SENSOR);
        map.put(DeviceType.CLIMATE_SENSOR, DeviceTypeAvro.CLIMATE_SENSOR);
        map.put(DeviceType.SWITCH_SENSOR, DeviceTypeAvro.SWITCH_SENSOR);
    }

}
