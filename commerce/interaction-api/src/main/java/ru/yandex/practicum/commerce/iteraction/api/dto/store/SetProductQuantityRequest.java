package ru.yandex.practicum.commerce.iteraction.api.dto.store;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class SetProductQuantityRequest {
    @NotBlank
    private UUID productId;
    @NotBlank
    private QuantityState quantityState;

}
