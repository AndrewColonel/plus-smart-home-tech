package ru.yandex.practicum.telemetry.analyzer.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Action;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Condition;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Scenario;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ActionRepository;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.service.handler.HubProcessorHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioAddedProcessorHandler implements HubProcessorHandler {

    private final ScenarioRepository scenarioRepository;
    private final ConditionRepository conditionRepository;
    private final ActionRepository actionRepository;

    @Override
    public String getRecordType() {
        return "ScenarioAddedEventAvro";

    }

    @Override
    public void handleRecord(HubEventAvro event) {
        ScenarioAddedEventAvro scenarioAddedEventAvro = (ScenarioAddedEventAvro) event.getPayload();
        Scenario scenario = Scenario.builder()
                .hubId(event.getHubId())
                .name(scenarioAddedEventAvro.getName())
                .conditions(new HashMap<>())
                .actions(new HashMap<>())
                .build();
        scenarioRepository.findByHubIdAndName(scenario.getHubId(), scenario.getName()).ifPresentOrElse(
                s -> {
                    log.info("Сценарий -{}- уже существует", s.getName());
                },
                () -> {
                    log.debug("Сценария {} еще не существует, создаем...", scenario.getName());
                    List<Condition> conditions = scenarioAddedEventAvro.getConditions().stream()
                            .map(this::toCondition)
                            .peek(c -> scenario.addCondition(c.getSensorId(), c))
                            .toList();
                    log.debug("Список кондиций - {}", conditions);
                    List<Condition> _conditions = conditionRepository.saveAll(conditions);
                    log.trace("добавлен список кондиций -{}- для сценария -{}-", _conditions, scenario.getName());

                    List<Action> actions = scenarioAddedEventAvro.getActions().stream()
                            .map(this::toAction)
                            .peek(a -> scenario.addAction(a.getSensorId(), a))
                            .toList();
                    log.debug("Список дейсвтий - {}", actions);
                    List<Action> _actions = actionRepository.saveAll(actions);
                    log.trace("Добавлен список действий -{}- для сценария -{}-", _actions, scenario.getName());

                    Scenario _scenario = scenarioRepository.save(scenario);
                    log.debug("Список состояний -{}- для сценария -{}-", _scenario.getConditions(), _scenario.getName());
                    log.debug("Список действий -{}- для сценария -{}-", _scenario.getActions(), _scenario.getName());

                    log.info("Сценарий -{}- создан", _scenario.getName());
                });
    }

    private Condition toCondition(ScenarioConditionAvro scenarioConditionAvro) {
        Condition condition = Condition.builder()
                .type(scenarioConditionAvro.getType().toString())
                .operation(scenarioConditionAvro.getOperation().toString())
                .sensorId(scenarioConditionAvro.getSensorId())
                .build();
        Object value = scenarioConditionAvro.getValue();
        if (Objects.nonNull(value)) {
            if (value instanceof Integer) condition.setValue((Integer) value);
            if (value instanceof Boolean) condition.setValue((Boolean) value ? 1 : 0);
        } else {
            condition.setValue(null);
        }
        return condition;
    }

    private Action toAction(DeviceActionAvro deviceActionAvro) {
        return Action.builder()
                .type(deviceActionAvro.getType().toString())
                .value(deviceActionAvro.getValue())
                .sensorId(deviceActionAvro.getSensorId())
                .build();
    }

}
