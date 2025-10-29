package ru.yandex.practicum.commerce.shopping.store.dal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.shopping.store.model.ProductCategory;
import ru.yandex.practicum.commerce.shopping.store.model.entity.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductReposiitory extends JpaRepository<Product, UUID> {
    Optional<Product> findByProductId(UUID productId);

    Page<Product> findAllByProductCategory(ProductCategory productCategory, Pageable page);

}
