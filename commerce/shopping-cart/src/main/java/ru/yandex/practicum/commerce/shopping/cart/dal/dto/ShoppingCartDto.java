package ru.yandex.practicum.commerce.shopping.cart.dal.dto;

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
public class ShoppingCartDto {
    @NotBlank
    private String shoppingCartId;
    @NotNull
    private Map<String, Integer> products;

}
