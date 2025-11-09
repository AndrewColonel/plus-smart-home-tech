package ru.yandex.practicum.commerce.warehouse.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.ShoppingCartDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.*;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.AddressDto;
import ru.yandex.practicum.commerce.iteraction.api.logging.Loggable;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse")
@AllArgsConstructor
@Validated
public class WarehouseController {

    private final WarehouseService service;

    @Loggable
    @PutMapping
    // Добавить новый товар на склад.
    public void createItem(@Valid @RequestBody NewProductInWarehouseRequest request) {
        service.createWarehouseItem(request);
    }

    @Loggable
    @PostMapping("/check")
    // Предварительно проверить что количество товаров на складе достаточно для данной
    // корзиный продуктов.
    public BookingProductsDto check(@Valid @RequestBody ShoppingCartDto shoppingCartDto) {
        return service.checkShoppingCart(shoppingCartDto);
    }

    @Loggable
    @PostMapping("/add")
    // Принять товар на склад.
    public void add(@Valid @RequestBody AddProductToWarehouseRequest request) {
        service.addProductsToWarehouse(request);
    }

    @Loggable
    @GetMapping("/address")
    // Предоставить адрес склада для расчёта доставки.
    public AddressDto getAddress() {
        return service.getWarehouseAddress();
    }

    @Loggable
    @PostMapping("/shipped")
    // Передать товары в доставку.
    public void shippedToDelivery(@Valid @RequestBody ShippedToDeliveryRequest request) {
        service.shippedToDeliveryOrder(request);
    }

    @Loggable
    @PostMapping("/return")
    // Принять возврат товаров на склад.
    public void returnProducts(@NotNull Map<UUID, Integer> products) {
        service.returnProductsOrder(products);
    }

    @Loggable
    @PostMapping("/assembly")
    // Собрать товары к заказу для подготовки к отправке.
    public BookingProductsDto assemblyProducts(@Valid @RequestBody AssemblyProductsForOrderRequest request) {
        return service.assemblyProductsOrder(request);
    }

}
