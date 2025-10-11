package ru.yandex.practicum.telemetry.analyzer.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.analyzer.dal.Entity.Scenario;
import ru.yandex.practicum.telemetry.analyzer.dal.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.service.handler.HubProcessorHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioRemovedProcesorHandler implements HubProcessorHandler {

    private final ScenarioRepository scenarioRepository;

    @Override
    public String getRecordType() {
        return "ScenarioRemovedEventAvro";

    }

    @Override
    public void handleRecord(HubEventAvro event) {
        ScenarioRemovedEventAvro scenarioRemovedEventAvro = (ScenarioRemovedEventAvro) event.getPayload();
        Scenario scenario = Scenario.builder()
                .hubId(event.getHubId())
                .name(scenarioRemovedEventAvro.getName())
                .build();
        scenarioRepository.findByHubIdAndName(scenario.getHubId(), scenario.getName()).ifPresentOrElse(
                v -> {
                    scenarioRepository.delete(v);
                    log.info("Сценарий {} удален", v.getName());
                },
                () -> log.info("Сценарий {} не найден", scenario.getName()));

    }
}
