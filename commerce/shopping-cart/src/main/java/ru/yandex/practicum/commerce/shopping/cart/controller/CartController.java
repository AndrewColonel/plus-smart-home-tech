package ru.yandex.practicum.commerce.shopping.cart.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.iteraction.api.logging.Loggable;
import ru.yandex.practicum.commerce.shopping.cart.dal.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.shopping.cart.dal.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.shopping.cart.service.CartServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@AllArgsConstructor
@Validated
@Slf4j
public class CartController {

    private final CartServiceImpl service;

    @Loggable
    @GetMapping
    // Получить актуальную корзину для авторизованного пользователя.
    public ShoppingCartDto get(@NotBlank @RequestParam String username) {
        // 401 - Имя пользователя не должно быть пустым
        return service.getUserCart(username);
    }

    @Loggable
    @PutMapping
    // Добавить товар в корзину.
    // 401 - Имя пользователя не должно быть пустым
    public ShoppingCartDto create(@NotBlank @RequestParam String username,
                                  @RequestBody Map<UUID, Integer> products) {

        return service.createUserCart(username, products);
    }

    @Loggable
    @DeleteMapping
    // Деактивация корзины товаров для пользователя.
    // 401 - Имя пользователя не должно быть пустым
    public void deactivate(@NotBlank @RequestParam String username) {
        service.deactivateUserCart(username);
    }

    @Loggable
    @PostMapping("/remove")
    // Удалить указанные товары из корзины пользователя.
    // 400 - Нет искомых товаров в корзине
    // 401 - Имя пользователя не должно быть пустым
    public ShoppingCartDto remove(@NotBlank @RequestParam String username,
                                  @NotBlank @RequestBody List<UUID> productIds) {

        return service.removeUserProducts(username, productIds);
    }

    @Loggable
    @PostMapping("/change-quantity")
    // Изменить количество товаров в корзине.
    // 400 - Нет искомых товаров в корзине
    // 401 - Имя пользователя не должно быть пустым
    public ShoppingCartDto update(@NotBlank @RequestParam String username,
                                  @Valid @RequestBody ChangeProductQuantityRequest request) {

        return service.updateUserCart(username, request);
    }

}
