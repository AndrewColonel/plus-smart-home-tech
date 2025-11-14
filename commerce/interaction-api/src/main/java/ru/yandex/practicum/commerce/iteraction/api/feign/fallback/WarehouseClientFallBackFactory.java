package ru.yandex.practicum.commerce.iteraction.api.feign.fallback;

import org.springframework.cloud.openfeign.FallbackFactory;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.WarehouseClient;

public class WarehouseClientFallBackFactory implements FallbackFactory<WarehouseClient> {
    @Override
    public WarehouseClient create(Throwable cause) {
        if (cause instanceof RuntimeException) {
            return new WarehouseClientFallBack();
        }
        return null;
    }
}
