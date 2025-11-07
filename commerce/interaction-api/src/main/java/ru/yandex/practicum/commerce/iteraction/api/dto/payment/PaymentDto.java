package ru.yandex.practicum.commerce.iteraction.api.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private UUID paymentId;
    private Double totalPayment;
    private Double deliveryTotal;
    private Double feeTotal;
}
