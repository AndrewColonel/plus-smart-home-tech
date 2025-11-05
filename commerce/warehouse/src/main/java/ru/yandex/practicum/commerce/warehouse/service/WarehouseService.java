package ru.yandex.practicum.commerce.warehouse.service;

import ru.yandex.practicum.commerce.iteraction.api.dto.common.ShoppingCartDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.BookingProductsDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.NewProductInWarehouseRequest;

public interface WarehouseService {

    void createWarehouseItem(NewProductInWarehouseRequest request);

    BookingProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto);

    void addProductsToWarehouse(AddProductToWarehouseRequest request);

    AddressDto getWarehouseAddress();
}
