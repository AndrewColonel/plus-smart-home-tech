package ru.yandex.practicum.telemetry.collector.model.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.model.HubEventType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {
    // Событие добавления сценария в систему. Содержит информацию о названии сценария, условиях и действиях.
    @NotBlank
    @Size(min = 3)
    private String name;
    @NotBlank
    private List<ScenarioCondition> conditions;
    @NotBlank
    private List<DeviceAction> actions;
//    @NotBlank
//    private HubEventType type;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }

//    private static ScenarioConditionAvro toAvro(ScenarioCondition event) {
//        return ScenarioConditionAvro.newBuilder()
//                .setOperation(event.getOperation())
//                .setSensorId()
//                .setType()
//                .setValue()
//                .build();
//    }
}
