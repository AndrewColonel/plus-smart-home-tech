package ru.yandex.practicum.commerce.payment.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ru.yandex.practicum.commerce.iteraction.api.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.payment.model.entity.Payment;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentMapper {

    public static PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .totalPayment(payment.getTotalPayment())
                .deliveryTotal(payment.getDeliveryTotal())
                .feeTotal(payment.getFeeTotal())
                .paymentState(payment.getPaymentState())
                .build();
    }

    public static Payment toEntity(PaymentDto paymentDto) {
        return Payment.builder()
                .paymentId(paymentDto.getPaymentId())
                .totalPayment(paymentDto.getTotalPayment())
                .deliveryTotal(paymentDto.getDeliveryTotal())
                .feeTotal(paymentDto.getFeeTotal())
                .paymentState(paymentDto.getPaymentState())
                .build();
    }

}
