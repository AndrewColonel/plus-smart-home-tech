package ru.yandex.practicum.commerce.shoppingstore.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.shoppingstore.dal.entity.Product;

public interface ProductReposiitory extends JpaRepository<Product, Long> {
}
