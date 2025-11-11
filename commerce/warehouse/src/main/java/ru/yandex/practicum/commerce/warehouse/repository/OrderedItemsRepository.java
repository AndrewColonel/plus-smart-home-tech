package ru.yandex.practicum.commerce.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.warehouse.model.entity.OrderBooking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderedItemsRepository extends JpaRepository<OrderBooking, Long> {

    List<OrderBooking> findByOrderId(UUID orderId);

    Optional<OrderBooking> findByProductId(UUID orderId);

}
