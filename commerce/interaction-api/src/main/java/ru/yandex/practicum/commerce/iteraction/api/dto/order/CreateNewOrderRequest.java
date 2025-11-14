package ru.yandex.practicum.commerce.iteraction.api.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.ShoppingCartDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.AddressDto;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewOrderRequest {

    @NotNull
    private ShoppingCartDto shoppingCart;
    @NotNull
    private AddressDto deliveryAddress;
}
