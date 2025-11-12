package ru.yandex.practicum.commerce.payment.model.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.iteraction.api.dto.payment.PaymentState;

import java.util.UUID;

@Entity(name = "payment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue
    private UUID paymentId;
    @Column(name = "order_id")
    private UUID orderId;
    @Column(name = "delivery_id")
    private UUID deliveryId;
    @Column(name = "total_payment")
    private Double totalPayment;
    @Column(name = "delivery_total")
    private Double deliveryTotal;
    @Column(name = "fee_total")
    private Double feeTotal;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_state")
    private PaymentState paymentState;
}
