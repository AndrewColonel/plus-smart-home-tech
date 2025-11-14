package ru.yandex.practicum.commerce.payment.service;

import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.payment.PaymentDto;

import java.util.UUID;

public interface PaymentService {
    // 200 Сформированная оплата заказа (переход в платежный шлюз)
    // 400 Недостаточно информации в заказе для расчёта
    PaymentDto getOrderPayment(OrderDto orderDto);

    // 200 Полная стоимость заказа
    // 400 Недостаточно информации в заказе для расчёта
    Double getOrderTotalCost(OrderDto orderDto);

    // 404 Заказ не найден
    void getOrderRefund(UUID paymentId);

    // 200 Расчёт стоимости товаров в заказе
    // 400 Недостаточно информации в заказе для расчёта
    Double getOrderProductCost(OrderDto orderDto);

    // 404 Заказ не найден
    void getOrderFailedRefund(UUID paymentId);
}
