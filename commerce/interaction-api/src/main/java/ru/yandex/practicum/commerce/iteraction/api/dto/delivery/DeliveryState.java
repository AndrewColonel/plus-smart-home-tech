package ru.yandex.practicum.commerce.iteraction.api.dto.delivery;

import java.util.Optional;

public enum DeliveryState {

    CREATED,
    IN_PROGRESS,
    DELIVERED,
    FAILED,
    CANCELLED;

    public static Optional<DeliveryState> from (String state) {
        for (DeliveryState deliveryState : DeliveryState.values()) {
            if (deliveryState.name().equalsIgnoreCase(state)) {
                return Optional.of(deliveryState);
            }
        }
        return Optional.empty();
    }
}
