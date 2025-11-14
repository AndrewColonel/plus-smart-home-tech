package ru.yandex.practicum.commerce.order.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.Address;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.AddressDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.order.model.entity.Order;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderMapper {

    public static OrderDto toDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .shoppingCartId(order.getShoppingCartId())
                .products(order.getProducts())
                .paymentId(order.getPaymentId())
                .deliveryId(order.getDeliveryId())
                .state(order.getState())
                .deliveryWeight(order.getDeliveryWeight())
                .deliveryVolume(order.getDeliveryVolume())
                .fragile(order.getFragile())
                .totalPrice(order.getTotalPrice())
                .deliveryPrice(order.getDeliveryPrice())
                .productPrice(order.getProductPrice())
//                .deliveryAddress(toDto(order.getDeliveryAddress()))
                .build();
    }


    public static Order toEntity(OrderDto orderDto) {
        return Order.builder()
                .orderId(orderDto.getOrderId())
                .shoppingCartId(orderDto.getShoppingCartId())
                .products(orderDto.getProducts())
                .paymentId(orderDto.getPaymentId())
                .deliveryId(orderDto.getDeliveryId())
                .state(orderDto.getState())
                .deliveryWeight(orderDto.getDeliveryWeight())
                .deliveryVolume(orderDto.getDeliveryVolume())
                .fragile(orderDto.getFragile())
                .totalPrice(orderDto.getTotalPrice())
                .deliveryPrice(orderDto.getDeliveryPrice())
                .productPrice(orderDto.getProductPrice())
//                .deliveryAddress(toEntity(orderDto.getDeliveryAddress()))
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


}
