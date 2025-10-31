package ru.yandex.practicum.commerce.iteraction.api.dto.warehouse;

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
