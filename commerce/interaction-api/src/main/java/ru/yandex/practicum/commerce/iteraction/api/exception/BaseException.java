package ru.yandex.practicum.commerce.iteraction.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.awt.event.FocusEvent;

@Getter
public class BaseException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String userMessage;

    public BaseException(String message, String userMessage, HttpStatus httpStatus, Throwable cause) {
        //  cause,  enableSuppression = true, writableStackTrace = true
        //  из публичного конструктора RunTimeException
        super(message, cause, true, true);
        this.userMessage = userMessage;
        this.httpStatus = httpStatus;

    }
}
