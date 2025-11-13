package ru.yandex.practicum.commerce.iteraction.api.dto.warehouse;

import lombok.*;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.Address;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookingDto {

    private UUID booking_id;
    private UUID orderId;
    private UUID deliveryId;
    private Address warehouseAddress;
    private OrderBookingState state;
    private Map<UUID, Integer> products;
}
