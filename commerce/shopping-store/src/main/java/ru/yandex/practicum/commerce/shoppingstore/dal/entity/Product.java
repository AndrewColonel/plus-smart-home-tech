package ru.yandex.practicum.commerce.shoppingstore.dal.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.shoppingstore.dal.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.dal.ProductState;
import ru.yandex.practicum.commerce.shoppingstore.dal.QuantityState;

import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    private UUID productId;
    @Column(name = "product_name", nullable = false)
    private String productName;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "image_src")
    private String imageSrc;
    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_state", nullable = false)
    private QuantityState quantityState;
    @Enumerated(EnumType.STRING)
    @Column(name = "product_state", nullable = false)
    private ProductState productState;
    @Enumerated(EnumType.STRING)
    @Column(name = "product_category", nullable = false)
    private ProductCategory productCategory;
    @Column(name = "price")
    private Double price;

}
