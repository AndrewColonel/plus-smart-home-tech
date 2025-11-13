package ru.yandex.practicum.commerce.order.model.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.Address;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderState;

import java.util.Map;
import java.util.UUID;

@Entity(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue
    private UUID orderId;
    @Column(name = "shopping_cart_id", nullable = false)
    private UUID shoppingCartId;

    @ElementCollection
    @CollectionTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> products;

    @Column(name = "payment_id")
    private UUID paymentId;
    @Column(name = "delivery_id")
    private UUID deliveryId;
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private OrderState state;
    @Column(name = "delivery_weight")
    private Double deliveryWeight;
    @Column(name = "delivery_volume")
    private Double deliveryVolume;
    @Column(name = "fragile")
    private Boolean fragile;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "delivery_price")
    private Double deliveryPrice;
    @Column(name = "product_price")
    private Double productPrice;

    @Embedded
    private Address deliveryAddress;


}
