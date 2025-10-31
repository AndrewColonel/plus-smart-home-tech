package ru.yandex.practicum.commerce.warehouse.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingProductsDto {

    @NotNull
    private Double deliveryweight;
    @NotNull
    private Double deliveryvolume;
    @NotNull
    private Boolean fragile;

}
