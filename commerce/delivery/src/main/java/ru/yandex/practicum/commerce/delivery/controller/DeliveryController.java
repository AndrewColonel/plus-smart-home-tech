package ru.yandex.practicum.commerce.delivery.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.delivery.service.DeliveryService;
import ru.yandex.practicum.commerce.iteraction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.iteraction.api.logging.Loggable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@AllArgsConstructor
@Validated
public class DeliveryController {

    private final DeliveryService service;

    // Создать новую доставку в БД.
    @Loggable
    @PutMapping
    public DeliveryDto createDelivery(@Valid @RequestBody DeliveryDto deliveryDto) {
        return service.createDeliveryOrder(deliveryDto);
    }

    // Эмуляция успешной доставки товара.
    @Loggable
    @PostMapping("/successful")
    public void getSuccessfulDelivery(@NotBlank @RequestBody UUID deliveryId) {
        service.getSuccessfulDeliveryOrder(deliveryId);
    }

    // Эмуляция получения товара в доставку.
    @Loggable
    @PostMapping("/picked")
    public void getPickedDelivery(@NotBlank @RequestBody UUID deliveryId) {
        service.getPickedDeliveryOrder(deliveryId);
    }

    // Эмуляция неудачного вручения товара
    @Loggable
    @PostMapping("/failed")
    public void getFailedDelivery(@NotBlank @RequestBody UUID deliveryId) {
        service.getFailedDeliveryOrder(deliveryId);
    }

    // Расчёт полной стоимости доставки заказа.
    @Loggable
    @PostMapping("/cost")
    public Double getDeliveryCost(@Valid @RequestBody OrderDto orderDto) {
        return service.getDeliveryCostOrder(orderDto);
    }

}
