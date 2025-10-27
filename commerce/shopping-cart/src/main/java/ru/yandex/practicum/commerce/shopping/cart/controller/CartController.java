package ru.yandex.practicum.commerce.shopping.cart.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.shopping.cart.dal.dto.ShoppingCartDto;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@AllArgsConstructor
public class CartController {

    @GetMapping
    // Получить актуальную корзину для авторизованного пользователя.
    public ShoppingCartDto get(@NotBlank @RequestParam String username) {

        return null;
    }


}
