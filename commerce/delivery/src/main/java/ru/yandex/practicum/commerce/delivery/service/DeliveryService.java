package ru.yandex.practicum.commerce.delivery.service;

import ru.yandex.practicum.commerce.iteraction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;

import java.util.UUID;

public interface DeliveryService {
    // 200 Указанная заявка с присвоенным идентификатором
    DeliveryDto createDeliveryOrder(DeliveryDto deliveryDto);

    // 404 Не найдена доставка
    void getSuccessfulDeliveryOrder(UUID deliveryId);

    // 404 Не найдена доставка для выдачи
    void getPickedDeliveryOrder(UUID deliveryId);

    // 404 Не найдена доставка для сбоя
    void getFailedDeliveryOrder(UUID deliveryId);

    // 200 Полная стоимость доставки заказа
    // 404 Не найдена доставка для расчёта
    Double getDeliveryCostOrder(OrderDto orderDto);
}
