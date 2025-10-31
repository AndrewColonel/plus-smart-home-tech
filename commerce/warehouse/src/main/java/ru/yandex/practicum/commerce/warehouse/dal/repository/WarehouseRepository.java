package ru.yandex.practicum.commerce.warehouse.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.warehouse.model.entity.WarehouseItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<WarehouseItem, Long> {

    Optional<WarehouseItem> findByProductId(UUID productId);

    List<WarehouseItem> findByProductIdIn(List<UUID> productIds);
}
