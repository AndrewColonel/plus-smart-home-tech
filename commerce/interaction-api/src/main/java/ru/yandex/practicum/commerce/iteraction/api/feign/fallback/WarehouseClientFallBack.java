package ru.yandex.practicum.commerce.iteraction.api.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.ShoppingCartDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.AddressDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.BookingProductsDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.WarehouseClient;

@Component
@Slf4j
public class WarehouseClientFallBack implements WarehouseClient {

    @Override
    public void createItem(NewProductInWarehouseRequest request) {
      log.warn("Fallback response: сервис create временно недоступен");
    }

    @Override
    public BookingProductsDto check(ShoppingCartDto shoppingCartDto) {
        log.warn("Fallback response: сервис check временно недоступен");
        return BookingProductsDto.builder()
                .deliveryvolume(0.0)
                .deliveryweight(0.0)
                .fragile(false)
                .build();
    }

    @Override
    public void add(AddProductToWarehouseRequest request) {
        log.warn("Fallback response: сервис add временно недоступен");
    }

    @Override
    public AddressDto getAddress() {
        log.warn("Fallback response: сервис getAddress временно недоступен");
        return null;
    }
}
