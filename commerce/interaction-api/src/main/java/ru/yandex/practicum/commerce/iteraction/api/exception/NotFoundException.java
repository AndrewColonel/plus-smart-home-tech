package ru.yandex.practicum.commerce.iteraction.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends BaseException {

    public NotFoundException(String message, String userMessage, HttpStatus httpStatus) {
        super(message, userMessage, httpStatus);

    }
}