package ru.yandex.practicum.commerce.warehouse.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "order_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_id")
    private UUID orderId;
    @Column(name = "delivery_id")
    private UUID deliveryId;
    @Column(name = "product_id")
    private UUID productId;
    @Column(name = "quantity")
    private Integer quantity;
}
