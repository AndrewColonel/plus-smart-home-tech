package ru.yandex.practicum.commerce.shopping.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.shopping.cart.model.entity.UserCart;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository extends JpaRepository<UserCart, UUID> {
    Optional<UserCart> findByUserName(String username);
}
