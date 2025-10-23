package ru.yandex.practicum.commerce.shoppingstore.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
