package ru.yandex.practicum.commerce.delivery.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.commerce.iteraction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository repository;

    // 200 Указанная заявка с присвоенным идентификатором
    @Override
    public DeliveryDto createDeliveryOrder(DeliveryDto deliveryDto) {

        return null;
    }

    // 404 Не найдена доставка
    @Override
    public void getSuccessfulDeliveryOrder(UUID deliveryId) {

    }

    // 404 Не найдена доставка для выдачи
    @Override
    public void getPickedDeliveryOrder(UUID deliveryId) {

    }

    // 404 Не найдена доставка для сбоя
    @Override
    public void getFailedDeliveryOrder(UUID deliveryId) {

    }

    // 200 Полная стоимость доставки заказа
    // 404 Не найдена доставка для расчёта
    @Override
    public Double getDeliveryCostOrder(OrderDto orderDto) {

        return null;
    }
}
