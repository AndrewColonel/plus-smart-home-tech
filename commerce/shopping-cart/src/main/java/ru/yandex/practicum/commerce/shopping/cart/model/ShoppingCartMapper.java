package ru.yandex.practicum.commerce.shopping.cart.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ru.yandex.practicum.commerce.shopping.cart.dal.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.shopping.cart.model.entity.ShoppingCart;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShoppingCartMapper {

    public static ShoppingCartDto toDto(ShoppingCart shoppingCart) {
        return ShoppingCartDto.builder()

                .build();
    }
}
