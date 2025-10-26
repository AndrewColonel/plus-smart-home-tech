package ru.yandex.practicum.commerce.shopping.store.exception;

public class EntityNotExistsException extends RuntimeException {
    public EntityNotExistsException(String message) {
        super(message);
    }
}
