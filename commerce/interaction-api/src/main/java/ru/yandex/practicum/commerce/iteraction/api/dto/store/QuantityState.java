package ru.yandex.practicum.commerce.iteraction.api.dto.store;

import java.util.Optional;

public enum QuantityState {
    // Статус, перечисляющий состояние остатка как свойства товара
    ENDED,
    FEW,
    ENOUGH,
    MANY;

    public static Optional<QuantityState> from(String state) {
        for (QuantityState quantityState : QuantityState.values()) {
            if (quantityState.name().equalsIgnoreCase(state)) {
                return Optional.of(quantityState);
            }
        }
        return Optional.empty();
    }
}
