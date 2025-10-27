package ru.yandex.practicum.commerce.shopping.cart.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.shopping.cart.model.entity.ShoppingCart;

import java.util.UUID;

public interface CartReposytory extends JpaRepository<ShoppingCart, UUID> {
}
