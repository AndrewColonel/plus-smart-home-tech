package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.yandex.practicum.telemetry.collector.model.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.HubEventType;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.service.handler.HubEventHandler;

@Slf4j
@AllArgsConstructor
public abstract class  BaseHubEventHandler  implements HubEventHandler {

    private final KafkaEventProducer kafkaEventProducer;

//    @Override
//    public void handle(HubEvent event) {
//        String topic = "telemetry.hubs.v1";
//        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, toAvro(event));
//        log.info(String.format("Создана запись для Avro %s",toAvro(event)));
//        kafkaEventProducer.getProducer().send(record);
////        kafkaEventProducer.getProducer().close();
//        kafkaEventProducer.getProducer().flush();
//    }

}
