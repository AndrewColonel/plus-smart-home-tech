package ru.yandex.practicum.commerce.warehouse.service;

import ru.yandex.practicum.commerce.iteraction.api.dto.common.ShoppingCartDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.*;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.AddressDto;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {

    void createWarehouseItem(NewProductInWarehouseRequest request);

    BookingProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto);

    void addProductsToWarehouse(AddProductToWarehouseRequest request);

    AddressDto getWarehouseAddress();

    void shippedToDeliveryOrder(ShippedToDeliveryRequest request);

    void returnProductsOrder(Map<UUID, Integer> products);

    BookingProductsDto assemblyProductsOrder(AssemblyProductsForOrderRequest request);
}
