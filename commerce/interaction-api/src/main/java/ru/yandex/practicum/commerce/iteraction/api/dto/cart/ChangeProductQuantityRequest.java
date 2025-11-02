package ru.yandex.practicum.commerce.iteraction.api.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeProductQuantityRequest {
    @NotBlank
    @NotNull
    private UUID productId;
    @NotNull
    private Integer newQuantity;
}
