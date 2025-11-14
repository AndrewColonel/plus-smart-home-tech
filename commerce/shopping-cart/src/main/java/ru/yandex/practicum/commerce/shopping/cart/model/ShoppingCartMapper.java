package ru.yandex.practicum.commerce.shopping.cart.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.ShoppingCartDto;
import ru.yandex.practicum.commerce.shopping.cart.model.entity.UserCart;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShoppingCartMapper {

    public static ShoppingCartDto toDto(UserCart entity) {
        return ShoppingCartDto.builder()
                .shoppingCartId(entity.getCartId())
                .products(entity.getProducts())
                .build();
    }

    public static UserCart toEntity(ShoppingCartDto dto) {
        return UserCart.builder()
                .cartId(dto.getShoppingCartId())
                .products(dto.getProducts())
                .build();
    }

}
