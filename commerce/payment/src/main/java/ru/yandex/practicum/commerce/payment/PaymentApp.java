package ru.yandex.practicum.commerce.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.commerce.iteraction.api.apierror.ApiExceptionHandler;
import ru.yandex.practicum.commerce.iteraction.api.logging.LoggingAspect;

@SpringBootApplication
@Import({ApiExceptionHandler.class, LoggingAspect.class,})
public class PaymentApp {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApp.class);
    }
}
