package ru.yandex.practicum.commerce.shopping.store.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
