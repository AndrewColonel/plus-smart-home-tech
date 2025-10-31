package ru.yandex.practicum.commerce.iteraction.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto {
    private String shoppingCartId;
    private Map<String, Integer> products;

}
