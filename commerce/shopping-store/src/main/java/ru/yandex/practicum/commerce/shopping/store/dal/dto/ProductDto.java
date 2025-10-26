package ru.yandex.practicum.commerce.shopping.store.dal.dto;


import lombok.*;
import ru.yandex.practicum.commerce.shopping.store.model.ProductCategory;
import ru.yandex.practicum.commerce.shopping.store.model.ProductState;
import ru.yandex.practicum.commerce.shopping.store.model.QuantityState;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    // Товар, продаваемый в интернет-магазине
    private String productId;
    @NotBlank
    private String productName;
    @NotBlank
    private String description;
    private String imageSrc;
    @NotBlank
    private QuantityState quantityState;
    @NotBlank
    private ProductState productState;
    @NotBlank
    private ProductCategory productCategory;
    @Positive
    private Double price;

}
