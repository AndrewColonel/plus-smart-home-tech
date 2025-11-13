package ru.yandex.practicum.commerce.warehouse.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.Address;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.AddressDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.DimensionDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.OrderBookingDto;
import ru.yandex.practicum.commerce.warehouse.model.entity.Dimension;
import ru.yandex.practicum.commerce.warehouse.model.entity.OrderBooking;
import ru.yandex.practicum.commerce.warehouse.model.entity.WarehouseItem;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WarehouseMapper {
    public static WarehouseItem toEntity(NewProductInWarehouseRequest request) {
        return WarehouseItem.builder()
                .productId(request.getProductId())
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

    public static AddressDto toDto(Address address) {
        return AddressDto.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .house(address.getHouse())
                .flat(address.getFlat())
                .build();
    }

    public static Address toEntity(AddressDto addressDto) {
        return Address.builder()
                .country(addressDto.getCountry())
                .city(addressDto.getCity())
                .street(addressDto.getStreet())
                .house(addressDto.getHouse())
                .flat(addressDto.getFlat())
                .build();
    }

    public OrderBooking toEntity(OrderBookingDto orderBookingDto) {
        return OrderBooking.builder()
                .booking_id(orderBookingDto.getBooking_id())
                .orderId(orderBookingDto.getOrderId())
                .deliveryId(orderBookingDto.getDeliveryId())
                .warehouseAddress(orderBookingDto.getWarehouseAddress())
                .state(orderBookingDto.getState())
                .build();
    }

    public OrderBookingDto toDto(OrderBooking orderBooking) {
        return OrderBookingDto.builder()
                .booking_id(orderBooking.getBooking_id())
                .orderId(orderBooking.getOrderId())
                .deliveryId(orderBooking.getDeliveryId())
                .warehouseAddress(orderBooking.getWarehouseAddress())
                .state(orderBooking.getState())
                .build();
    }

}
