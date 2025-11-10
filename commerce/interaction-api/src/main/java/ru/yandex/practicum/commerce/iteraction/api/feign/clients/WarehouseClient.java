package ru.yandex.practicum.commerce.iteraction.api.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.ShoppingCartDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.*;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.AddressDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse"
//        ,fallbackFactory = WarehouseClientFallBackFactory.class
)
public interface WarehouseClient {

    @PutMapping("/api/v1/warehouse")
    void createItem(@RequestBody NewProductInWarehouseRequest request);

    @PostMapping("/api/v1/warehouse/check")
    BookingProductsDto check(@RequestBody ShoppingCartDto shoppingCartDto);

    @PostMapping("/api/v1/warehouse/add")
    void add(@RequestBody AddProductToWarehouseRequest request);

    @GetMapping("/api/v1/warehouse/address")
    AddressDto getAddress();

    @PostMapping("/shipped")
    void shippedToDelivery(@Valid @RequestBody ShippedToDeliveryRequest request);


    @PostMapping("/return")
    void returnProducts(@NotNull Map<UUID, Integer> products);

    @PostMapping("/assembly")
    BookingProductsDto assemblyProducts(@Valid @RequestBody AssemblyProductsForOrderRequest request);
}
