package ru.yandex.practicum.commerce.shopping.cart.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.shopping.cart.model.entity.Cart;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserName(String username);
}
