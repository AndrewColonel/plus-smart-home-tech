package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hub.ScenarioAddedEvent;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;
import ru.yandex.practicum.telemetry.collector.service.handler.HubEventHandler;

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
        return ScenarioAddedEventAvro.newBuilder()
                .setName(_event.getName())
//                .setConditions(_event.getConditions())
//                .setActions(_event.getActions())
                .build();
    }

//    @Override
//    public ScenarioAddedEventAvro toAvro(HubEvent event) {
//        ScenarioAddedEvent _event = (ScenarioAddedEvent) event;
//        List<ScenarioConditionAvro> conditionAvros = event.getConditions().stream()
//                .map(this::toConditionAvro)
//                .collect(Collectors.toList());
//
//        List<DeviceActionAvro> actionAvros = event.getActions().stream()
//                .map(this::toActionAvro)
//                .collect(Collectors.toList());
//
//        return ScenarioAddedEventAvro.newBuilder()
//                .setName(event.getName())
//                .setConditions(conditionAvros)
//                .setActions(actionAvros)
//                .build();
//    }
//
//    private ScenarioConditionAvro toConditionAvro(ScenarioCondition c) {
//        Object value = c.getValue(); // предположим, что getValue() возвращает Object
//        UnionNullIntBoolean valueUnion = null;
//
//        if (value instanceof Integer) {
//            valueUnion = UnionNullIntBoolean.newBuilder().setInt((Integer) value).build();
//        } else if (value instanceof Boolean) {
//            valueUnion = UnionNullIntBoolean.newBuilder().setBoolean((Boolean) value).build();
//        } // иначе остаётся null
//
//        return ScenarioConditionAvro.newBuilder()
//                .setSensorId(c.getSensorId())
//                .setType(toConditionTypeAvro(c.getType()))
//                .setOperation(toConditionOperationAvro(c.getOperation()))
//                .setValue(valueUnion)
//                .build();
//    }
//
//    private DeviceActionAvro toActionAvro(DeviceAction a) {
//        UnionNullInt valueUnion = null;
//        if (a.getValue() != null) {
//            valueUnion = UnionNullInt.newBuilder().setInt(a.getValue()).build();
//        }
//        return DeviceActionAvro.newBuilder()
//                .setSensorId(a.getSensorId())
//                .setType(toActionTypeAvro(a.getType()))
//                .setValue(valueUnion)
//                .build();
//    }



}
