package ru.yandex.practicum.commerce.payment.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

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
    @Column(name = "delivery_total")
    private Double deliveryTotal;
    @Column(name = "fee_total")
    private Double feeTotal;
}
