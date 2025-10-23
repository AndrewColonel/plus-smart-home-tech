package ru.yandex.practicum.commerce.shoppingstore.dal.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.shoppingstore.dal.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.dal.ProductState;
import ru.yandex.practicum.commerce.shoppingstore.dal.QuantityState;


@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    @Column(name = "product_Id")
    private String productId;
    @Column(name = "product_name", nullable = false)
    private String productName;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "image_src")
    private String imageSrc;
    //    @ManyToOne
//    @JoinColumn(name = "quantity_state_id")
//    private String quantityState;
//    @ManyToOne
//    @JoinColumn(name = "product_state_id")
//    private String productState;
//    @ManyToOne
//    @JoinColumn(name = "product_category_id")
//    private String productCategory;
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
