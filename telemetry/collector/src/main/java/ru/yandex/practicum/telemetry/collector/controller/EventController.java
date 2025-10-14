package ru.yandex.practicum.telemetry.collector.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.config.KafkaConfig;
import ru.yandex.practicum.telemetry.collector.service.handler.HubEventHandler;
import ru.yandex.practicum.telemetry.collector.service.handler.SensorEventHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

@GrpcService
@Slf4j
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {
    private final Map<HubEventProto.PayloadCase, HubEventHandler> hubEventHandlers;
    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorEventHandlers;

    private final Producer<String, SpecificRecordBase> producer;
    private final KafkaConfig.ProducerConfig producerConfig;

    @Autowired
    public EventController(List<HubEventHandler> hubEventHandlers, List<SensorEventHandler> sensorEventHandlers,
                           KafkaConfig kafkaConfig) {
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType,
                        Function.identity()));

        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType,
                        Function.identity()));
        this.producerConfig = kafkaConfig.getProducerConfig();
        this.producer = new KafkaProducer<>(producerConfig.getProperties());

    }

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            if (sensorEventHandlers.containsKey(request.getPayloadCase())) {
                SensorEventHandler handler = sensorEventHandlers.get(request.getPayloadCase());
                log.info("Выбран обработчик события от сенсоров  {}", handler.getClass().getSimpleName());
                log.info("Тип события сенсора {}", request.getPayloadCase());
                SensorEventAvro sensorEventAvro = handler.handle(request);

                String topic = producerConfig.getSensortopic();
                ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, sensorEventAvro);
                log.info("Объект Avro для отправки в брокер {} в топик {}", sensorEventAvro, topic);
                Future<RecordMetadata> metadataFuture = producer.send(record);
                log.info("Состояние отправки: {} ", metadataFuture.isDone());
                producer.flush();
                log.info("Состояние отправки: {} ", metadataFuture.isDone());
            } else {
                throw new IllegalArgumentException("Не могу найти обработчик для события " + request.getPayloadCase());
            }
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            if (hubEventHandlers.containsKey(request.getPayloadCase())) {
                HubEventHandler handler = hubEventHandlers.get(request.getPayloadCase());
                log.info("Выбран обработчик события от хаба {}", handler.getClass().getSimpleName());
                log.info("Тип события хаба {}", request.getPayloadCase());
                HubEventAvro hubEventAvro = handler.handle(request);

                String topic = producerConfig.getHubtopic();
                ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, hubEventAvro);
                log.info("Объект Avro для отправки в брокер {} в топик {}", hubEventAvro, topic);
                Future<RecordMetadata> metadataFuture = producer.send(record);
                log.info("Состояние отправки: {} ", metadataFuture.isDone());
                producer.flush();
                log.info("Состояние отправки: {} ", metadataFuture.isDone());

            } else {
                throw new IllegalArgumentException("Не могу найти обработчик для события " + request.getPayloadCase());
            }
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }
}