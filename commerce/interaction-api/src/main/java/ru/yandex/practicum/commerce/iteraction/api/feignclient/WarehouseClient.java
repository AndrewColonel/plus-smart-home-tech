package ru.yandex.practicum.commerce.iteraction.api.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.ShoppingCartDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.BookingProductsDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.NewProductInWarehouseRequest;

@FeignClient(name = "warehouse")
public interface WarehouseClient {

    @PutMapping
    void create(@RequestBody NewProductInWarehouseRequest request);

    @PostMapping("/check")
    BookingProductsDto check(@RequestBody ShoppingCartDto shoppingCartDto);

    @PostMapping("/add")
    void add(@RequestBody AddProductToWarehouseRequest request);

    @GetMapping("/address")
    AddressDto getAddress();
}
