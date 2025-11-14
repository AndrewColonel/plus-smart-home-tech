package ru.yandex.practicum.commerce.iteraction.api.exception;

import org.springframework.http.HttpStatus;

public class NoDeliveryFoundException extends BaseException {

    public NoDeliveryFoundException(String message, String userMessage, HttpStatus httpStatus, Throwable cause) {
      super(message, userMessage, httpStatus, cause);

    }
}
