package ru.yandex.practicum.commerce.iteraction.api.exception;

import org.springframework.http.HttpStatus;

public class NoProductsInCartException extends BaseException {

    public NoProductsInCartException(String message, String userMessage, HttpStatus httpStatus, Throwable cause) {
        super(message, userMessage, httpStatus, cause);

    }
}
