package ru.yandex.practicum.commerce.iteraction.api.exception;

import org.springframework.http.HttpStatus;

public class NoAuthorizedUserException extends BaseException {

    public NoAuthorizedUserException(String message, String userMessage, HttpStatus httpStatus, Throwable cause) {
      super(message, userMessage, httpStatus, cause);

    }
}
