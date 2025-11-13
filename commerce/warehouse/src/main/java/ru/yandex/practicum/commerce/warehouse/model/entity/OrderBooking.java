package ru.yandex.practicum.commerce.warehouse.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.Address;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity(name = "booking")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBooking {
    @Id
    @GeneratedValue
    private UUID booking_id;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Embedded
    private Address warehouseAddress;

    @ElementCollection
    @CollectionTable(
            name = "order_booking",
            joinColumns = @JoinColumn(name = "booking_id")
    )
    @MapKeyColumn(name = "product_id")  // ключ (UUID)
    @Column(name = "quantity")          // значения (Integer)
    private Map<UUID, Integer> products = new HashMap<>();
}
