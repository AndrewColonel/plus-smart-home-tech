package ru.yandex.practicum.commerce.iteraction.api.dto.order;

import java.util.Optional;

public enum OrderState {

    NEW,
    ON_PAYMENT,
    ON_DELIVERY,
    DONE,
    DELIVERED,
    ASSEMBLED,
    PAID,
    COMPLETED,
    DELIVERY_FAILED,
    ASSEMBLY_FAILED,
    PAYMENT_FAILED,
    PRODUCT_RETURNED,
    CANCELED;

    public static Optional<OrderState> from(String state) {
        for (OrderState orderState : OrderState.values()) {
            if (orderState.name().equalsIgnoreCase(state)) {
                return Optional.of(orderState);
            }
        }
        return Optional.empty();
    }
}
