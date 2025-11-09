package ru.yandex.practicum.commerce.iteraction.api.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.PaymentClient;

import java.util.UUID;

@Component
@Slf4j
public class PaymentClientFallBack implements PaymentClient {


    @Override
    public PaymentDto getPayment(OrderDto orderDto) {
        log.warn("Fallback PaymentClient response: сервис getPayment временно недоступен");
        return new PaymentDto();
    }

    @Override
    public Double getTotalCost(OrderDto orderDto) {
        log.warn("Fallback PaymentClient response: сервис getTotalCost временно недоступен");
        return 0.0;
    }

    @Override
    public void getRefund(UUID paymentId) {
        log.warn("Fallback PaymentClient response: сервис getRefund временно недоступен");

    }

    @Override
    public Double getProductCost(OrderDto orderDto) {
        log.warn("Fallback PaymentClient response: сервис getProductCost временно недоступен");
        return 0.0;
    }

    @Override
    public void getFailedRefund(UUID paymentId) {
        log.warn("Fallback PaymentClient response: сервис getFailedRefund временно недоступен");
    }
}
