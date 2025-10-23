package ru.yandex.practicum.commerce.shoppingstore.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
