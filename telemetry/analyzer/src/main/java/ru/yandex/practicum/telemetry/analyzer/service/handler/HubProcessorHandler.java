package ru.yandex.practicum.telemetry.analyzer.service.handler;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubProcessorHandler {
    String getRecordType();

    void handleRecord(HubEventAvro event);
}
