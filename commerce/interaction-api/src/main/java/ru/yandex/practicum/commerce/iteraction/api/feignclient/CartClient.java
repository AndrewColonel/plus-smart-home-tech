package ru.yandex.practicum.commerce.iteraction.api.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.iteraction.api.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.ShoppingCartDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart")
public interface CartClient {
    @GetMapping
    ShoppingCartDto get(@RequestParam String username);

    @PutMapping
    ShoppingCartDto create(@RequestParam String username,
                           @RequestBody Map<UUID, Integer> products);

    @DeleteMapping
    void deactivate(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto remove(@NotBlank @RequestParam String username,
                           @NotBlank @RequestBody List<UUID> productIds);

    @PostMapping("/change-quantity")
    ShoppingCartDto update(@NotBlank @RequestParam String username,
                           @Valid @RequestBody ChangeProductQuantityRequest request);

}

