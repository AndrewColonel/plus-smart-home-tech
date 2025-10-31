package ru.yandex.practicum.commerce.iteraction.api.dto.store;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SetProductQuantityRequest {
    @NotBlank
    private String productId;
    @NotBlank
    private QuantityState quantityState;

}
