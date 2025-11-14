package ru.yandex.practicum.commerce.delivery.model.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.Address;
import ru.yandex.practicum.commerce.iteraction.api.dto.delivery.DeliveryState;

import java.util.UUID;

@Entity
@Table(name = "delivery")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue
    private UUID deliveryId;
    // для полноценной работы с адресами необходима отдельная таблица или даже модуль,
    // но с требуемой в ТЗ бизнес логикой ограничемся встроенным классом
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "from_country")),
            @AttributeOverride(name = "city", column = @Column(name =  "from_city")),
            @AttributeOverride(name = "street", column = @Column(name =  "from_street")),
            @AttributeOverride(name = "house", column = @Column(name =  "from_house")),
            @AttributeOverride(name = "flat", column = @Column(name =  "from_flat"))
    })
    private Address fromAddress;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name =  "to_country")),
            @AttributeOverride(name = "city", column = @Column(name =  "to_city")),
            @AttributeOverride(name = "street", column = @Column(name =  "to_street")),
            @AttributeOverride(name = "house", column = @Column(name =  "to_house")),
            @AttributeOverride(name = "flat", column = @Column(name =  "to_flat"))
    })
    private Address toAddress;
    @Column(name = "order_id")
    private UUID orderId;
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_state")
    private DeliveryState deliveryState;


}
