package ru.yandex.practicum.commerce.iteraction.api.dto.warehouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippedToDeliveryRequest {

    @NotBlank
    private UUID orderId;
    @NotBlank
    private UUID deliveryId;
}
