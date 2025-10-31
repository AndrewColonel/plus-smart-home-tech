package ru.yandex.practicum.commerce.shopping.cart.service;

import ru.yandex.practicum.commerce.shopping.cart.dal.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.iteraction.api.common.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;

import java.util.UUID;

public interface CartService {
    ShoppingCartDto getUserCart(String username);

    ShoppingCartDto createUserCart(String username, Map<UUID, Integer> products);

    void deactivateUserCart(String username);

    ShoppingCartDto removeUserProducts(String username, List<UUID> productIds);

    ShoppingCartDto updateUserCart(String username, ChangeProductQuantityRequest request);

}
