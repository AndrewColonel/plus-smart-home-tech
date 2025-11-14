package ru.yandex.practicum.commerce.iteraction.api.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    @NotBlank
    private UUID orderId;
    private UUID shoppingCartId;
    @NotNull
    private Map<UUID, Integer> products;
    private UUID paymentId;
    private UUID deliveryId;
    private OrderState state;
    private Double deliveryWeight;
    private Double deliveryVolume;
    private Boolean fragile;
    private Double totalPrice; // Общая стоимость товары+налог+доставка
    private Double deliveryPrice; // стоимость доставки;
    private Double productPrice; // Стоимость товаров в заказе
//    private AddressDto deliveryAddress;


}
