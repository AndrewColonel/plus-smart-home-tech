package ru.yandex.practicum.commerce.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.commerce.iteraction.api.apierror.ApiExceptionHandler;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.OrderClient;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.WarehouseClient;
import ru.yandex.practicum.commerce.iteraction.api.logging.LoggingAspect;

@SpringBootApplication
@Import({ApiExceptionHandler.class, LoggingAspect.class,})
@EnableFeignClients(clients = {OrderClient.class, WarehouseClient.class})
public class DeliveryApp {
    public static void main(String[] args) {
        SpringApplication.run(DeliveryApp.class);
    }
}
