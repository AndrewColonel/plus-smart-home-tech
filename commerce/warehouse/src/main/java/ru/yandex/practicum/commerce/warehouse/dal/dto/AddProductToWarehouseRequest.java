package ru.yandex.practicum.commerce.warehouse.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProductToWarehouseRequest {
    @NotBlank
    private String productId;
    @NotNull
    private Integer quantity;
}
