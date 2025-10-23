package ru.yandex.practicum.commerce.shoppingstore.dal.dto;


import lombok.*;
import ru.yandex.practicum.commerce.shoppingstore.dal.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.dal.ProductState;
import ru.yandex.practicum.commerce.shoppingstore.dal.QuantityState;

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
