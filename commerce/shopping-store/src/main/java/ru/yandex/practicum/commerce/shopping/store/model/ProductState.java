package ru.yandex.practicum.commerce.shopping.store.model;

import java.util.Optional;

public enum ProductState {
    // Статус товара
    ACTIVE,
    DEACTIVATE;

    public static Optional<ProductState> from(String state) {
        for (ProductState productState : ProductState.values()) {
            if (productState.name().equalsIgnoreCase(state)) {
                return Optional.of(productState);
            }
        }
        return Optional.empty();
    }
}
