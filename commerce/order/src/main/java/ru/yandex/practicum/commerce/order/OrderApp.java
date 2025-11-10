package ru.yandex.practicum.commerce.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.commerce.iteraction.api.apierror.ApiExceptionHandler;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.CartClient;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.DeliveryClient;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.PaymentClient;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.WarehouseClient;
import ru.yandex.practicum.commerce.iteraction.api.logging.LoggingAspect;

@SpringBootApplication
@Import({ApiExceptionHandler.class, LoggingAspect.class,})
@EnableFeignClients(clients = {DeliveryClient.class,
        PaymentClient.class,
        WarehouseClient.class,
        CartClient.class })
public class OrderApp {
    public static void main(String[] args) {
        SpringApplication.run(OrderApp.class);
    }

}
