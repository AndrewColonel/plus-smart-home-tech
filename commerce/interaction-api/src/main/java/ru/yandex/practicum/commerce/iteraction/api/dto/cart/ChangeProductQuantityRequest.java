package ru.yandex.practicum.commerce.iteraction.api.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeProductQuantityRequest {
    @NotBlank
    private String shoppingCartId;
    @NotBlank
    private String username;
    @NotNull
    private Map<String, Integer> products;

}
