package ru.yandex.practicum.commerce.shopping.cart.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ru.yandex.practicum.commerce.shopping.cart.dal.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.shopping.cart.model.entity.Cart;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ShoppingCartMapper {

    public static ShoppingCartDto toDto(Cart entity) {
        return ShoppingCartDto.builder()
                .shoppingCartId(entity.getCartId().toString())
                .products(entity.getProducts().entrySet().stream()
                        .collect(Collectors.toMap(
                                entry ->
                                        entry.getKey().toString(),
                                Map.Entry::getValue)))
                .build();
    }

    public static Cart toEntity(ShoppingCartDto dto) {
        return Cart.builder()
                .cartId(UUID.fromString(dto.getShoppingCartId()))
                .products(dto.getProducts().entrySet().stream()
                        .collect(Collectors.toMap(
                                entry ->
                                        UUID.fromString(entry.getKey()),
                                Map.Entry::getValue)))
                .build();
    }

}
