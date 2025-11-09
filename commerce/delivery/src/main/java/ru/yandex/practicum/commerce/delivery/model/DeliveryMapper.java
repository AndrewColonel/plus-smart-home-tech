package ru.yandex.practicum.commerce.delivery.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ru.yandex.practicum.commerce.delivery.model.entity.Address;
import ru.yandex.practicum.commerce.delivery.model.entity.Delivery;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.AddressDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.delivery.DeliveryDto;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliveryMapper {

    public static DeliveryDto toDto(Delivery delivery) {
        return DeliveryDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .fromAddress(toDto(delivery.getFromAddress()))
                .toAddress(toDto(delivery.getToAddress()))
                .orderId(delivery.getOrderId())
                .deliveryState(delivery.getDeliveryState())
                .build();
    }

    public static Delivery toEntity(DeliveryDto deliveryDto) {
        return Delivery.builder()
                .deliveryId(deliveryDto.getDeliveryId())
                .fromAddress(toEntity(deliveryDto.getFromAddress()))
                .toAddress(toEntity(deliveryDto.getToAddress()))
                .orderId(deliveryDto.getOrderId())
                .deliveryState(deliveryDto.getDeliveryState())
                .build();
    }

    private static AddressDto toDto(Address address) {
        return AddressDto.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .house(address.getHouse())
                .flat(address.getFlat())
                .build();
    }

    private static Address toEntity(AddressDto addressDto) {
        return Address.builder()
                .country(addressDto.getCountry())
                .city(addressDto.getCity())
                .street(addressDto.getStreet())
                .house(addressDto.getHouse())
                .flat(addressDto.getFlat())
                .build();
    }
}
