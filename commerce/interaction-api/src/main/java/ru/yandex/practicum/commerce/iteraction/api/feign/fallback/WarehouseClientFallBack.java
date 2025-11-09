package ru.yandex.practicum.commerce.iteraction.api.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.ShoppingCartDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.*;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.AddressDto;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.WarehouseClient;

import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class WarehouseClientFallBack implements WarehouseClient {

    @Override
    public void createItem(NewProductInWarehouseRequest request) {
      log.warn("Fallback WarehouseClient response: сервис createItem временно недоступен");
    }

    @Override
    public BookingProductsDto check(ShoppingCartDto shoppingCartDto) {
        log.warn("Fallback WarehouseClient response: сервис check временно недоступен");
        return BookingProductsDto.builder()
                .deliveryvolume(0.0)
                .deliveryweight(0.0)
                .fragile(false)
                .build();
    }

    @Override
    public void add(AddProductToWarehouseRequest request) {
        log.warn("Fallback WarehouseClient response: сервис add временно недоступен");
    }

    @Override
    public AddressDto getAddress() {
        log.warn("Fallback WarehouseClient response: сервис getAddress временно недоступен");
        return null;
    }

    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest request) {
        log.warn("Fallback WarehouseClient response: сервис shippedToDelivery временно недоступен");
    }

    @Override
    public void returnProducts(Map<UUID, Integer> products) {
        log.warn("Fallback WarehouseClient response: сервис returnProducts временно недоступен");
    }

    @Override
    public BookingProductsDto assemblyProducts(AssemblyProductsForOrderRequest request) {
        log.warn("Fallback WarehouseClient response: сервис assemblyProducts временно недоступен");
        return new BookingProductsDto();
    }
}
