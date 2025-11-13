package ru.yandex.practicum.commerce.warehouse.model.entity;

import java.util.Optional;

public enum OrderBookingState {

    CREATED,
    FAILED,
    CANCELLED;

    public static Optional<OrderBookingState> from (String state) {
        for (OrderBookingState orderBookingState : OrderBookingState.values()) {
            if (orderBookingState.name().equalsIgnoreCase(state)) {
                return Optional.of(orderBookingState);
            }
        }
        return Optional.empty();
    }
}
