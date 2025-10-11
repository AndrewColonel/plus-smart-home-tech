package ru.yandex.practicum.telemetry.analyzer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.analyzer.service.HubEventProcessor;


@Component
@RequiredArgsConstructor
public class AnalyserRunner implements CommandLineRunner {
    final HubEventProcessor hubEventProcessor;
//    final SnapshotProcessor snapshotProcessor;

    @Override
    public void run(String... args) throws Exception {

        hubEventProcessor.run();

    }
}
