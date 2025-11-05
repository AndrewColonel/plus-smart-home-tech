package ru.yandex.practicum.commerce.iteraction.api.feignclient;

import org.springframework.cloud.openfeign.FallbackFactory;

public class WarehouseClientFallBackFactory implements FallbackFactory<WarehouseClient> {
    @Override
    public WarehouseClient create(Throwable cause) {
        if (cause instanceof RuntimeException) {
            return new WarehouseClientFallBack();
        }
        return null;
    }
}
