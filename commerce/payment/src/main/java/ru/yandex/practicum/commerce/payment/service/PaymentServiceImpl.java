package ru.yandex.practicum.commerce.payment.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.payment.repository.PaymentRepository;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;

    // 200 Сформированная оплата заказа (переход в платежный шлюз)
    // 400 Недостаточно информации в заказе для расчёта
    @Override
    public PaymentDto getOrderPayment(OrderDto orderDto) {

        return null;
    }

    // 200 Полная стоимость заказа
    // 400 Недостаточно информации в заказе для расчёта
    @Override
    public Double getOrderTotalCost(OrderDto orderDto) {

        return null;
    }

    // 404 Заказ не найден
    @Override
    public void getOrderRefund(UUID paymentId) {

    }

    // 200 Расчёт стоимости товаров в заказе
    // 400 Недостаточно информации в заказе для расчёта
    @Override
    public Double getOrderProductCost(OrderDto orderDto) {

        return null;
    }

    // 404 Заказ не найден
    @Override
    public void getOrderFailedRefund(UUID paymentId) {

    }


}
