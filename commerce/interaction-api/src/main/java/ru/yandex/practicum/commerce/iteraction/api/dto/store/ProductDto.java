package ru.yandex.practicum.commerce.iteraction.api.dto.store;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    // Товар, продаваемый в интернет-магазине
    private UUID productId;
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
