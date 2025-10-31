package ru.yandex.practicum.commerce.warehouse.service;

import ru.yandex.practicum.commerce.iteraction.api.common.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.warehouse.dal.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.dal.dto.AddressDto;
import ru.yandex.practicum.commerce.warehouse.dal.dto.BookingProductsDto;
import ru.yandex.practicum.commerce.warehouse.dal.dto.NewProductInWarehouseRequest;

public interface WarehouseService {

    void createWarehouseItem(NewProductInWarehouseRequest request);

    BookingProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto);

    void addProductsToWarehouse(AddProductToWarehouseRequest request);

    AddressDto getWarehouseAddress();
}
