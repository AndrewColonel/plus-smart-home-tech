package ru.yandex.practicum.telemetry.hubrouter;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;

@GrpcService
@Slf4j
public class HubRouterService extends HubRouterControllerGrpc.HubRouterControllerImplBase {

    @Override
    public void handleDeviceAction(DeviceActionRequest request, StreamObserver<Empty> responseObserver) {
        try {
            log.info("----------------------------------------------------------------------");
            log.info("Запрос получен ");
            log.info("сценарий - {}", request.getScenarioName());
            log.info("устройство - {}", request.getAction().getSensorId());
            log.info("действие - {}", request.getAction().getType());
            log.info("величина - {}", request.getAction().getValue());
            log.info("----------------------------------------------------------------------");
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
