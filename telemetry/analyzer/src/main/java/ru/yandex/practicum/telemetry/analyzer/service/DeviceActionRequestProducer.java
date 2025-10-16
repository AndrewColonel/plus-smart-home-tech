package ru.yandex.practicum.telemetry.analyzer.service;

import com.google.protobuf.Empty;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;

@Slf4j
@Service
public class DeviceActionRequestProducer {
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public DeviceActionRequestProducer(@GrpcClient("hub-router")
                                       HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient) {
        this.hubRouterClient = hubRouterClient;
    }
    public void send(DeviceActionRequest request) {
        log.trace("Отправляю данные: {}", request.getAllFields());
        Empty response = hubRouterClient.handleDeviceAction(request);
        log.trace("Получил ответ от коллектора: {}", response);
    }

}