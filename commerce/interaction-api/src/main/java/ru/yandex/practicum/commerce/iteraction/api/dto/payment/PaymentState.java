package ru.yandex.practicum.commerce.iteraction.api.dto.payment;

import java.util.Optional;

public enum PaymentState {

    PENDING,
    SUCCESS,
    FAILED;

    public static Optional<PaymentState> from(String state) {
        for (PaymentState paymentState : PaymentState.values()) {
            if (paymentState.name().equalsIgnoreCase(state)) {
                return Optional.of(paymentState);
            }
        }
        return Optional.empty();
    }

}
