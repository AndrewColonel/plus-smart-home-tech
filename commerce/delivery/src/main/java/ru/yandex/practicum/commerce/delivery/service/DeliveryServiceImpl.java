package ru.yandex.practicum.commerce.delivery.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.delivery.model.entity.Delivery;
import ru.yandex.practicum.commerce.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.AddressDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.delivery.DeliveryState;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.commerce.iteraction.api.exception.NoDeliveryFoundException;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.OrderClient;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.WarehouseClient;

import java.util.NoSuchElementException;
import java.util.UUID;

import static ru.yandex.practicum.commerce.delivery.model.DeliveryMapper.toDto;
import static ru.yandex.practicum.commerce.delivery.model.DeliveryMapper.toEntity;

@Service
@AllArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    // базовая стоимость равна 5.0:
    private static final double BASE_DELIVERY_COST = 5.0;

    private final DeliveryRepository repository;

    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    // 200 Указанная заявка с присвоенным идентификатором
    @Override
    public DeliveryDto createDeliveryOrder(DeliveryDto deliveryDto) {
        // Сохранить сведения о доставке в БД и вернуть идентификатор созданного
        // объекта доставки для использования с сервисом склада в дальнейшем:
        deliveryDto.setDeliveryState(DeliveryState.CREATED);
        return toDto(repository.save(toEntity(deliveryDto)));
    }

    // 404 Не найдена доставка
    @Override
    public void getSuccessfulDeliveryOrder(UUID deliveryId) {
        // после получения вероятного обратного звонка от
        // системы доставки, подтверждающего успешную доставку заказа со склада.
        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        repository.save(delivery);
    }

    // 404 Не найдена доставка для выдачи
    @Override
    public void getPickedDeliveryOrder(UUID deliveryId) {
        // после того, как служба доставки получит заказ-наряд на доставку.
        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        repository.save(delivery);
        // связать идентификатор доставки с внутренней учётной системой
        // через вызов соответствующего метода склада.
        warehouseClient.shippedToDelivery(ShippedToDeliveryRequest.builder()
                .orderId(delivery.getOrderId())
                .deliveryId(deliveryId)
                .build());
        // необходимо изменить статус заказа на ASSEMBLED в сервисе заказов
        orderClient.orderAssembly(delivery.getOrderId());
    }

    // 404 Не найдена доставка для сбоя
    @Override
    public void getFailedDeliveryOrder(UUID deliveryId) {
        // обратный звонок от службы доставки и потребуется изменить
        // статус заказа в системе на «Неудачная доставка»
        Delivery delivery = getDeliveryById(deliveryId);
        delivery.setDeliveryState(DeliveryState.FAILED);
        repository.save(delivery);
    }

    // 200 Полная стоимость доставки заказа
    // 404 Не найдена доставка для расчёта
    @Override
    public Double getDeliveryCostOrder(OrderDto orderDto) {
        Delivery delivery = getDeliveryById(orderDto.getDeliveryId());
        // базовая стоимость равна 5.0:
        double deliveryCost = BASE_DELIVERY_COST;
        // каждое новое действие выполняем с суммой, которая получается на предыдущем этапе
        // Умножаем базовую стоимость на число, зависящее от адреса склада
        // Если адрес склада содержит название ADDRESS_1, то умножаем на 1
        // Если адрес склада содержит название ADDRESS_2, то умножаем на 2
        deliveryCost = deliveryCost + deliveryCost
                * (delivery.getFromAddress().getCity()
                .equalsIgnoreCase("ADDRESS_1") ? 1 : 2);
        // Если в заказе есть признак хрупкости, умножаем сумму на 0.2
        // и складываем с полученным на предыдущем шаге итогом.
        deliveryCost = deliveryCost + deliveryCost
                * ((orderDto.getFragile()) ? 0.2 : 0);
        // Добавляем к сумме, полученной на предыдущих шагах, вес заказа, умноженный на 0.3
        deliveryCost = deliveryCost + deliveryCost
                * (orderDto.getDeliveryWeight() * 0.3);
        // Складываем с полученным на прошлом шаге итогом объём, умноженный на 0.2
        deliveryCost = deliveryCost + deliveryCost
                * (orderDto.getDeliveryVolume() * 0.2);
        // Если нужно доставить на ту же улицу, то стоимость доставки не увеличивается.
        // Иначе её нужно умножить на 0.2 и сложить с полученным на предыдущем шаге итогом
        AddressDto warehouseAddress = warehouseClient.getAddress();
        deliveryCost = deliveryCost + deliveryCost
                * ((delivery.getToAddress().getStreet()
                .equalsIgnoreCase(warehouseAddress.getStreet())) ? 0 : 0.2);

        return deliveryCost;
    }

    // вспомогательные методы
    private Delivery getDeliveryById(UUID deliveryId) {
        return repository.findById(deliveryId).orElseThrow(() ->
                new NoDeliveryFoundException(
                        String.format("Доставка с id %S не найдена", deliveryId),
                        "404 Не найдена доставка для расчёта",
                        HttpStatus.NOT_FOUND, new NoSuchElementException("Такой доставки  нет в базе")));
    }
}
