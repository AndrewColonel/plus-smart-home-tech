package ru.yandex.practicum.commerce.warehouse.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.iteraction.api.common.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.warehouse.dal.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.dal.dto.AddressDto;
import ru.yandex.practicum.commerce.warehouse.dal.dto.BookingProductsDto;
import ru.yandex.practicum.commerce.warehouse.dal.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/warehouse")
@AllArgsConstructor
@Validated
public class WarehouseController {

    private final WarehouseService service;

    @PutMapping
    // Добавить новый товар на склад.
    public void create(@Valid @RequestBody NewProductInWarehouseRequest request) {
        service.createWarehouseItem(request);
    }

    @PostMapping("/check")
    // Предварительно проверить что количество товаров на складе достаточно для данной
    // корзиный продуктов.
    public BookingProductsDto check(@Valid @RequestBody ShoppingCartDto shoppingCartDto) {
        return service.checkShoppingCart(shoppingCartDto);
    }

    @PostMapping("/add")
    // Принять товар на склад.
    public void add(@Valid @RequestBody AddProductToWarehouseRequest request) {
        service.addProductsToWarehouse(request);
    }

    @GetMapping("/address")
    // Предоставить адрес склада для расчёта доставки.
    public AddressDto getAddress() {
        return service.getWarehouseAddress();
    }
}
