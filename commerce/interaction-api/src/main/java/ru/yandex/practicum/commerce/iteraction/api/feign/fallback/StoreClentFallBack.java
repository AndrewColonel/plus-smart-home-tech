package ru.yandex.practicum.commerce.iteraction.api.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.iteraction.api.dto.store.ProductCategory;
import ru.yandex.practicum.commerce.iteraction.api.dto.store.ProductDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.store.SetProductQuantityRequest;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.StoreClient;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class StoreClentFallBack implements StoreClient {
    @Override
    public Page<ProductDto> getAll(ProductCategory category, Pageable page) {
        log.warn("Fallback StoreClient response: сервис getAll временно недоступен");
        return null;
    }

    @Override
    public ProductDto create(ProductDto productDto) {
        log.warn("Fallback StoreClient response: сервис create временно недоступен");
        return new ProductDto();
    }

    @Override
    public ProductDto update(ProductDto productDto) {
        log.warn("Fallback StoreClient response: сервис update временно недоступен");
        return new ProductDto();
    }

    @Override
    public boolean remove(UUID productId) {
        log.warn("Fallback StoreClient response: сервис remove временно недоступен");
        return false;
    }

    @Override
    public boolean setStatus(SetProductQuantityRequest request) {
        log.warn("Fallback StoreClient response: сервис setStatus временно недоступен");
        return false;
    }

    @Override
    public ProductDto getById(UUID productId) {
        log.warn("Fallback StoreClient response: сервис getById временно недоступен");
        return new ProductDto();
    }

    @Override
    public List<ProductDto> getList(List<UUID> productIds) {
        log.warn("Fallback StoreClient response: сервис getList временно недоступен");
        return List.of();
    }
}
