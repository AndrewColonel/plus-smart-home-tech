package ru.yandex.practicum.commerce.shopping.store.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
