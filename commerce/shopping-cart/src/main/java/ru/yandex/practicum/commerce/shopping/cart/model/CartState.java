package ru.yandex.practicum.commerce.shopping.cart.model;

import java.util.Optional;

public enum CartState {
    // Статус товара
    ACTIVE,
    DEACTIVATED;

    public static Optional<CartState> from(String state) {
        for (CartState productState : CartState.values()) {
            if (productState.name().equalsIgnoreCase(state)) {
                return Optional.of(productState);
            }
        }
        return Optional.empty();
    }
}
