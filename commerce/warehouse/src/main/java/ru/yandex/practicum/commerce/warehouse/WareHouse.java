package ru.yandex.practicum.commerce.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.commerce.iteraction.api.apierror.ApiExceptionHandler;
import ru.yandex.practicum.commerce.iteraction.api.logging.LoggingAspect;

@SpringBootApplication
@Import({ApiExceptionHandler.class, LoggingAspect.class})
public class WareHouse {
    public static void main(String[] args) {
        SpringApplication.run(WareHouse.class);
    }
}
