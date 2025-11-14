package ru.yandex.practicum.commerce.iteraction.api.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.OrderClient;

import java.util.UUID;

@Component
@Slf4j
public class OrderClientFallBack implements OrderClient {
    @Override
    public Page<OrderDto> getAll(String username, Pageable page) {
        log.warn("Fallback OrderClient response: сервис getAll временно недоступен");
        return null;
    }

    @Override
    public OrderDto create(CreateNewOrderRequest request) {
        log.warn("Fallback OrderClient response: сервис create временно недоступен");
        return new OrderDto();
    }

    @Override
    public OrderDto returnRequest(ProductReturnRequest request) {
        log.warn("Fallback OrderClient response: сервис returnRequest временно недоступен");
        return new OrderDto();
    }

    @Override
    public OrderDto orderPayment(UUID orderId) {
        log.warn("Fallback OrderClient response: сервис orderPayment временно недоступен");
        return new OrderDto();
    }

    @Override
    public OrderDto orderPaymentFailed(UUID orderId) {
        log.warn("Fallback OrderClient response: сервис orderPaymentFailed временно недоступен");
        return new OrderDto();
    }

    @Override
    public OrderDto orderDelivery(UUID orderId) {
        log.warn("Fallback OrderClient response: сервис orderDelivery временно недоступен");
        return new OrderDto();
    }

    @Override
    public OrderDto orderDeliveryFailed(UUID orderId) {
        log.warn("Fallback OrderClient response: сервис orderDeliveryFailed временно недоступен");
        return new OrderDto();
    }

    @Override
    public OrderDto orderCompleted(UUID orderId) {
        log.warn("Fallback OrderClient response: сервис orderCompleted временно недоступен");
        return new OrderDto();
    }

    @Override
    public OrderDto orderCalculatedTotal(UUID orderId) {
        log.warn("Fallback OrderClient response: сервис orderCalculatedTotal временно недоступен");
        return new OrderDto();
    }

    @Override
    public OrderDto orderCalculatedDelivery(UUID orderId) {
        log.warn("Fallback OrderClient response: сервис orderCalculatedDelivery временно недоступен");
        return new OrderDto();
    }

    @Override
    public OrderDto orderAssembly(UUID orderId) {
        log.warn("Fallback OrderClient response: сервис orderAssembly временно недоступен");
        return new OrderDto();
    }

    @Override
    public OrderDto orderAssemblyFailed(UUID orderId) {
        log.warn("Fallback DeliveryClient response: сервис orderAssemblyFailed временно недоступен");
        return new OrderDto();
    }
}
