package ru.yandex.practicum.commerce.shopping.store.model;

import java.util.Optional;

public enum ProductCategory {
    // Категория товара
    LIGHTING,
    CONTROL,
    SENSORS;

    public static Optional<ProductCategory> from(String category) {
        for (ProductCategory productCategory : ProductCategory.values()) {
            if (productCategory.name().equalsIgnoreCase(category)) {
                return Optional.of(productCategory);
            }
        }
        return Optional.empty();
    }
}
