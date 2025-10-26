package ru.yandex.practicum.commerce.shoppingstore.dal.dto;

import lombok.Data;
import ru.yandex.practicum.commerce.shoppingstore.dal.QuantityState;

import javax.validation.constraints.NotBlank;

@Data
public class SetProductQuantityRequest {
    @NotBlank
    private String productId;
    @NotBlank
    private QuantityState quantityState;

}
