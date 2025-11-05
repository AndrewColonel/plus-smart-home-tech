package ru.yandex.practicum.commerce.warehouse.model.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.UUID;

@Entity
@Table(name = "warehouse_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_id", nullable = false)
    private UUID productId;
    @Column(name = "fragile", nullable = false)
    private Boolean fragile;
    @Embedded
    private Dimension dimension;
    @Column(name = "weight", nullable = false)
    private Double weight;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
