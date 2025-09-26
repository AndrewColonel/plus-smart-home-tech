package ru.yandex.practicum.telemetry.collector.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.yandex.practicum.telemetry.collector.model.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.SensorEventType;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.sensors.SensorEvent;

import ru.yandex.practicum.telemetry.collector.service.handler.HubEventHandler;
import ru.yandex.practicum.telemetry.collector.service.handler.SensorEventHandler;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
//@AllArgsConstructor
@Validated
@RequestMapping("/events")
@Slf4j
public class EventController {
    private final Map<HubEventType, HubEventHandler> hubEventHandlers;
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlers;

    public EventController(List<HubEventHandler> hubEventHandlers, List<SensorEventHandler> sensorEventHandlers) {
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType,
                        Function.identity()));

        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType,
                        Function.identity()));
    }

//    private final DeviceAddedHubEventHandler deviceAddedHubEventHandler;
//    private final DeviceRemovedHubEventHandler deviceRemovedHubEventHandler;
//    private final ScenarioAddedHubEventHandler scenarioAddedHubEventHandler;
//    private final ScenarioRemovedHubEventHandler scenarioRemovedHubEventHandler;

    @PostMapping("/sensors")
    public void collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        log.info("SensorEvent - json: {}", toJson(event));
        log.info("SensorEvent - toString: {}", event.toString());
        if (sensorEventHandlers.containsKey(event.getType())) {
            sensorEventHandlers.get(event.getType()).handle(event);
        } else {
            throw new IllegalArgumentException("не могу найти обработчик для событий сенсоров");
        }
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@Valid @RequestBody HubEvent event) {
        log.info("HubEvent - json: {}", toJson(event));
        log.info("HubEvent - toString: {}", event.toString());
//        switch (event.getType()) {
//            case DEVICE_ADDED -> deviceAddedHubEventHandler.handle(event);
//            case DEVICE_REMOVED -> deviceRemovedHubEventHandler.handle(event);
//            case SCENARIO_ADDED -> scenarioAddedHubEventHandler.handle(event);
//            case SCENARIO_REMOVED -> scenarioRemovedHubEventHandler.handle(event);
//            default -> throw new IllegalArgumentException("не могу найти обработчик");
//    }

        if (hubEventHandlers.containsKey(event.getType())) {
            hubEventHandlers.get(event.getType()).handle(event);
        } else {
            throw new IllegalArgumentException("не могу найти обработчик для событий хаба");
        }
    }

    private <T> String toJson(T object) {
        String json;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.writerWithDefaultPrettyPrinter();
        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            json = String.format("Ошибка сериализации %s, пеолученный объект %s:",
                    exception.getMessage(), object.toString());
        }
        return json;
    }

}
