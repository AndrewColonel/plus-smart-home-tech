package ru.yandex.practicum.commerce.shopping.cart;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.commerce.iteraction.api.apierror.ApiExceptionHandler;
import ru.yandex.practicum.commerce.iteraction.api.logging.LoggingAspect;

@SpringBootApplication
@ConfigurationPropertiesScan
@Import({ApiExceptionHandler.class, LoggingAspect.class})
@EnableFeignClients
public class ShoppingCart {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingCart.class);
    }
}
