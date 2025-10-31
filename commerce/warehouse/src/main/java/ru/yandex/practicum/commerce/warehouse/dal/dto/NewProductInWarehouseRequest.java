package ru.yandex.practicum.commerce.warehouse.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewProductInWarehouseRequest {

    @NotBlank
    private String productId;
    @NotBlank
    private Boolean fragile;
    @NotNull
    private DimensionDto dimension;
    @NotNull
    @Min(1)
    private Double weight;
}
