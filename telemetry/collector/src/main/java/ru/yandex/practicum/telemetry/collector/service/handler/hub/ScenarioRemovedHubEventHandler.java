package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.service.handler.HubEventHandler;

@Component
public class ScenarioRemovedHubEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> implements HubEventHandler {

    public ScenarioRemovedHubEventHandler(KafkaEventProducer kafkaEventProducer) {
        super(kafkaEventProducer);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }

    @Override
    public ScenarioRemovedEventAvro toAvro(HubEvent event) {
        ScenarioRemovedEvent _eevent = (ScenarioRemovedEvent) event;
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(_eevent.getName())
                .build();

    }
}
