package ru.yandex.practicum.commerce.iteraction.api.feignclient;

import org.springframework.cloud.openfeign.FallbackFactory;
import ru.yandex.practicum.commerce.iteraction.api.exception.BaseException;

public class WarehouseClientFallBackFactory implements FallbackFactory<WarehouseClient> {
    @Override
    public WarehouseClient create(Throwable cause) {
        if (cause instanceof BaseException) {
            return new WarehouseClientFallBack();
        }
        return null;
    }
}
