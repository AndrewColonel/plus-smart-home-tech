package ru.yandex.practicum.commerce.shoppingstore.dal.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class SetProductQuantityRequest {
    @NotBlank
    private String productId;
    @NotBlank
    private String quantityState;

}
