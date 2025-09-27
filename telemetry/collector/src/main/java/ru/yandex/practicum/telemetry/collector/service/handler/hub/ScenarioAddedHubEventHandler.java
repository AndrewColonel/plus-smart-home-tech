package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.common.EnumMapper;
import ru.yandex.practicum.telemetry.collector.model.hub.DeviceAction;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hub.ScenarioAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.ScenarioCondition;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.service.handler.HubEventHandler;

import java.util.List;
import java.util.stream.Collectors;

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
                .setType(EnumMapper.toAvroEnum(ConditionTypeAvro.class,
                        scenarioCondition.getType().toString()))
                .setOperation(EnumMapper.toAvroEnum(ConditionOperationAvro.class,
                        scenarioCondition.getOperation().toString()))
                .setValue(scenarioCondition.getValue())
                .build();
    }

    private DeviceActionAvro toActionAvro(DeviceAction deviceAction) {
             return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(EnumMapper.toAvroEnum(ActionTypeAvro.class,
                        deviceAction.getType().toString()))
                .setValue(deviceAction.getValue())
                .build();
    }
}
