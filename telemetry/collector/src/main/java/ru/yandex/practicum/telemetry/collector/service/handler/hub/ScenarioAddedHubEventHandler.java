package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.common.EnumMapper;
import ru.yandex.practicum.telemetry.collector.service.handler.HubEventHandler;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScenarioAddedHubEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> implements HubEventHandler {

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public ScenarioAddedEventAvro toAvro(HubEventProto event) {
        ScenarioAddedEventProto _event = event.getScenarioAdded();
        List<ScenarioConditionAvro> conditionAvros = _event.getConditionList().stream()
                .map(this::toConditionAvro)
                .collect(Collectors.toList());

        List<DeviceActionAvro> actionAvros = _event.getActionList().stream()
                .map(this::toActionAvro)
                .collect(Collectors.toList());

        return ScenarioAddedEventAvro.newBuilder()
                .setName(_event.getName())
                .setConditions(conditionAvros)
                .setActions(actionAvros)
                .build();
    }

    private ScenarioConditionAvro toConditionAvro(ScenarioConditionProto scenarioCondition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioCondition.getSensorId())
                .setType(EnumMapper.toAvroEnum(ConditionTypeAvro.class,
                        scenarioCondition.getType().toString()))
                .setOperation(EnumMapper.toAvroEnum(ConditionOperationAvro.class,
                        scenarioCondition.getOperation().toString()))
                .setValue(
                        switch (scenarioCondition.getValueCase()) {
                            case BOOL_VALUE -> scenarioCondition.getBoolValue();
                            case INT_VALUE -> scenarioCondition.getIntValue();
                            case VALUE_NOT_SET -> null;
                        })
                .build();
    }

    private DeviceActionAvro toActionAvro(DeviceActionProto deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(EnumMapper.toAvroEnum(ActionTypeAvro.class,
                        deviceAction.getType().toString()))
                .setValue(deviceAction.getValue())
                .build();
    }
}
