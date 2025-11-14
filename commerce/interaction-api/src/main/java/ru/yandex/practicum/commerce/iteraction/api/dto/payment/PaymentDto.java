package ru.yandex.practicum.commerce.iteraction.api.dto.payment;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDto {

    private UUID paymentId;
    private Double totalPayment; // общая стоимость товаров
    private Double deliveryTotal; // стоимость доставки;
    private Double feeTotal; // общая стоимость (товар+ налог + доставка)
    private PaymentState paymentState;
}
