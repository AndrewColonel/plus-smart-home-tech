package ru.yandex.practicum.commerce.iteraction.api.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.iteraction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.DeliveryClient;

import java.util.UUID;

@Component
@Slf4j
public class DeliveryClientFallBack implements DeliveryClient {

    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        log.warn("Fallback DeliveryClient response: сервис createDelivery временно недоступен");
        return new DeliveryDto();
    }

    @Override
    public void getSuccessfulDelivery(UUID deliveryId) {
        log.warn("Fallback DeliveryClient response: сервис getSuccessfulDelivery временно недоступен");
    }

    @Override
    public void getPickedDelivery(UUID deliveryId) {
        log.warn("Fallback DeliveryClient response: сервис getPickedDelivery временно недоступен");
    }

    @Override
    public void getFailedDelivery(UUID deliveryId) {
        log.warn("Fallback DeliveryClient response: сервис getFailedDelivery временно недоступен");
    }

    @Override
    public Double getDeliveryCost(OrderDto orderDto) {
        log.warn("Fallback DeliveryClient response: сервис getDeliveryCost временно недоступен");
        return 0.0;
    }
}
