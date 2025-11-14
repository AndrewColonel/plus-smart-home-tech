package ru.yandex.practicum.commerce.iteraction.api.dto.warehouse;

import lombok.*;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.AddressDto;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookingProductsDto {

    @NotNull
    private Double deliveryweight;
    @NotNull
    private Double deliveryvolume;
    @NotNull
    private Boolean fragile;

    private AddressDto fromAddress;

}
