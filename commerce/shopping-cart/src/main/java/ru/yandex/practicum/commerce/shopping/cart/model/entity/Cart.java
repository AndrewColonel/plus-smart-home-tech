package ru.yandex.practicum.commerce.shopping.cart.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity(name = "shopping_cart")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue
    private UUID cartId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ElementCollection
    @CollectionTable(
            name = "cart_product",
            joinColumns = @JoinColumn(name = "cart_id")
    )
    @MapKeyColumn(name = "product_id")  // колонка для ключа (UUID)
    @Column(name = "quantity")          // колонка для значения (Integer)
    private Map<UUID, Integer> products = new HashMap<>();

}
