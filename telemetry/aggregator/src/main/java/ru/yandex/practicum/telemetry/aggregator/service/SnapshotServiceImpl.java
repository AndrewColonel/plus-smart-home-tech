package ru.yandex.practicum.telemetry.aggregator.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class SnapshotServiceImpl implements SnapshotService {

    // набор снапшотов с ключом - хаб
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    @Override
    // метод по обработке полученной записи
    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
//        Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();
        SensorsSnapshotAvro snapShot;
        // Проверяем, есть ли снапшот для event.getHubId()
        // Если снапшот есть, то достаём его
        // Если нет, то созадём новый
        if (snapshots.containsKey(event.getHubId())) {
            snapShot = snapshots.get(event.getHubId());
            // Проверяем, есть ли в снапшоте данные для event.getId()
            if (snapShot.getSensorsState().containsKey(event.getId())) {
                // Если данные есть, то достаём их в переменную oldState
                SensorStateAvro oldState = snapShot.getSensorsState().get(event.getId());
                // Проверка, если oldState.getTimestamp() произошёл позже, чем
                // event.getTimestamp() или oldState.getData() равен event.getPayload(),
                // то ничего обнавлять не нужно, выходим из метода вернув Optional.empty()
                if (oldState.getTimestamp().isAfter(event.getTimestamp())
                        || oldState.getData().equals(event.getPayload())) {
                    log.trace("Данные телементрии в событии {} не изменились", event);
                    log.trace("Старые показания. Время {}, Данные {}", oldState.getTimestamp(), oldState.getData());
                    log.trace("Новые показания. Время {}, Данные {}", event.getTimestamp(), event.getPayload());
                    return Optional.empty();
                }
            }
            // если дошли до сюда, значит, пришли новые данные и снапшот нужно обновить
            // Создаём экземпляр SensorStateAvro на основе данных события
            // Добавляем полученный экземпляр в снапшот
            SensorStateAvro newState = SensorStateAvro.newBuilder()
                    .setTimestamp(event.getTimestamp())
                    .setData(event.getPayload())
                    .build();

            snapShot.getSensorsState().put(event.getId(), newState);
            // Обновляем таймстемп снапшота таймстемпом из события
            snapShot.setTimestamp(event.getTimestamp());
        } else {
            // Создаем новый снапшот
            Map<String, SensorStateAvro> sensorStats = new HashMap<>();
            sensorStats.put(event.getId(), SensorStateAvro.newBuilder()
                    .setTimestamp(event.getTimestamp())
                    .setData(event.getPayload())
                    .build());

            snapShot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(event.getHubId())
                    .setTimestamp(event.getTimestamp())
                    .setSensorsState(sensorStats)
                    .build();
            // Добавляем его в мапу
            snapshots.put(event.getHubId(), snapShot);
        }
        log.trace("<++ Обработано сообщение от сенсоров, создан новый снапшот {} ++>", snapShot);
        // Возвращаем снапшот - Optional.of(snapshot)
        return Optional.of(snapShot);
    }
}
