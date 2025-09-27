package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.model.hub.DeviceAction;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hub.ScenarioAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.ScenarioCondition;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.service.handler.HubEventHandler;

import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.telemetry.collector.common.EnumMapper.*;

@Component
public class ScenarioAddedHubEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> implements HubEventHandler {

    public ScenarioAddedHubEventHandler(KafkaEventProducer kafkaEventProducer) {
        super(kafkaEventProducer);
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    public ScenarioAddedEventAvro toAvro(HubEvent event) {
        ScenarioAddedEvent _event = (ScenarioAddedEvent) event;
        List<ScenarioConditionAvro> conditionAvros = _event.getConditions().stream()
                .map(this::toConditionAvro)
                .collect(Collectors.toList());

        List<DeviceActionAvro> actionAvros = _event.getActions().stream()
                .map(this::toActionAvro)
                .collect(Collectors.toList());

        return ScenarioAddedEventAvro.newBuilder()
                .setName(_event.getName())
                .setConditions(conditionAvros)
                .setActions(actionAvros)
                .build();
    }

    private ScenarioConditionAvro toConditionAvro(ScenarioCondition scenarioCondition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioCondition.getSensorId())
                .setType(toConditionTypeAvro(scenarioCondition.getType()))
                .setOperation(toConditionOperationAvro(scenarioCondition.getOperation()))
                .setValue(scenarioCondition.getValue())
                .build();
    }

    private DeviceActionAvro toActionAvro(DeviceAction deviceAction) {
             return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(toActionTypeAvro(deviceAction.getType()))
                .setValue(deviceAction.getValue())
                .build();
    }
}
