package ru.yandex.practicum.commerce.iteraction.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotEnoughInfoInOrderToCalculateException extends BaseException {

    public NotEnoughInfoInOrderToCalculateException(String message, String userMessage, HttpStatus httpStatus, Throwable cause) {
        super(message, userMessage, httpStatus, cause);
    }
}
