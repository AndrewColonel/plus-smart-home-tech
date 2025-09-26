package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.HubEventType;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.service.handler.HubEventHandler;

@Component
@AllArgsConstructor
@Slf4j
public class ScenarioAddedHubEventHandler  implements HubEventHandler {

    private final KafkaEventProducer kafkaEventProducer;

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }

//    private ScenarioAddedEventAvro toAvro(HubEvent event) {
//        ScenarioAddedEvent _event = (ScenarioAddedEvent) event;
//        return ScenarioAddedEventAvro.newBuilder()
//                .setName(_event.getName())
//                .setConditions(_event.getConditions())
//                .setActions(_event.getActions())
//                .build();
//    }

    @Override
    public void handle(HubEvent event) {
//        String topic = "telemetry.hubs.v1";
//        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, toAvro(event));
//        log.info(String.format("Создана запись для Avro %s",toAvro(event)));
//        kafkaEventProducer.getProducer().send(record);
////        kafkaEventProducer.getProducer().close();
//        kafkaEventProducer.getProducer().flush();
    }
}
