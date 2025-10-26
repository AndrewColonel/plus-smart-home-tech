package ru.yandex.practicum.commerce.shopping.store.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
