package ru.yandex.practicum.commerce.shopping.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.commerce.iteraction.api.apierror.ApiExceptionHandler;
import ru.yandex.practicum.commerce.iteraction.api.feign.fallback.WarehouseClientFallBackFactory;
import ru.yandex.practicum.commerce.iteraction.api.logging.LoggingAspect;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.WarehouseClient;

@SpringBootApplication
@Import({ApiExceptionHandler.class, LoggingAspect.class,
        WarehouseClientFallBackFactory.class})
@EnableFeignClients(clients = WarehouseClient.class)
public class ShoppingCart {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingCart.class);
    }
}
