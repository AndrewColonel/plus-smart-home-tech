package ru.yandex.practicum.commerce.shoppingstore.dal.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.commerce.shoppingstore.dal.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.dal.entity.Product;


import java.util.List;
import java.util.Optional;

public interface ProductReposiitory extends JpaRepository<Product, Long> {
    Optional<Product> findByProductId(String productId);

    List<Product> findAllByProductCategory(ProductCategory productCategory, Pageable page);

}
