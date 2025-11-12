package ru.yandex.practicum.commerce.iteraction.api.dto.cart;

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
