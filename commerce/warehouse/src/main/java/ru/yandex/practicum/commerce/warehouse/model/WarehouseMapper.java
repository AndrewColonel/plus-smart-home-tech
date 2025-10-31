package ru.yandex.practicum.commerce.warehouse.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ru.yandex.practicum.commerce.warehouse.dal.dto.DimensionDto;
import ru.yandex.practicum.commerce.warehouse.dal.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.model.entity.Dimension;
import ru.yandex.practicum.commerce.warehouse.model.entity.WarehouseItem;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WarehouseMapper {
    public static WarehouseItem toEntity(NewProductInWarehouseRequest request) {
        return WarehouseItem.builder()
                .productId(UUID.fromString(request.getProductId()))
                .fragile(request.getFragile())
                .dimension(toEntity(request.getDimension()))
                .weight(request.getWeight())
                .quantity(0)
                .build();
    }

    private static Dimension toEntity(DimensionDto dimensionDto) {
        return Dimension.builder()
                .width(dimensionDto.getWidth())
                .height(dimensionDto.getHeight())
                .depth(dimensionDto.getDepth())
                .build();
    }

}
