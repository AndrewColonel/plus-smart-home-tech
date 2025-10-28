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
import java.util.Map;
import java.util.UUID;

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
        return service.getCart(username);
    }

    @Loggable
    @PutMapping
    // Добавить товар в корзину.
    public ShoppingCartDto create(@NotBlank @RequestParam String username,
                                  @RequestBody Map<String, Integer> products) {
        // 401 - Имя пользователя не должно быть пустым
        return service.createCart(username, products);
    }

    @Loggable
    @DeleteMapping
    // Деактивация корзины товаров для пользователя.
    public void delete(@NotBlank @RequestParam String userbane) {
        // 401 - Имя пользователя не должно быть пустым
    }

    @Loggable
    @PostMapping("/remove")
    // Удалить указанные товары из корзины пользователя.
    public ShoppingCartDto remove(@NotBlank @RequestParam String userbane,
                                  @NotBlank @RequestBody List<UUID> productIds) {
        // 401 - Имя пользователя не должно быть пустым
        return null;
    }

    @Loggable
    @PostMapping("/change-quantity")
    // Изменить количество товаров в корзине.
    public ShoppingCartDto update(@NotBlank @RequestParam String userbane,
                                  @Valid @RequestBody ChangeProductQuantityRequest request) {

        // 400 - Нет искомых товаров в корзине
        // 401 - Имя пользователя не должно быть пустым
        return null;
    }

}
