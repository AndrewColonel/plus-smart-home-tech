package ru.yandex.practicum.commerce.warehouse.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DimensionDto {

    @NotNull
    @Min(1)
    private Double width;
    @NotNull
    @Min(1)
    private Double height;
    @NotNull
    @Min(1)
    private Double depth;

}
