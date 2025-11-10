package ru.yandex.practicum.commerce.iteraction.api.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.iteraction.api.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@FeignClient(name = "delivery")
public interface DeliveryClient {

    // Создать новую доставку в БД.
    @PutMapping("/api/v1/delivery")
    DeliveryDto createDelivery(@Valid @RequestBody DeliveryDto deliveryDto);

    // Эмуляция успешной доставки товара.
    @PostMapping("/api/v1/delivery/successful")
    void getSuccessfulDelivery(@NotBlank @RequestBody UUID deliveryId);

    // Эмуляция получения товара в доставку.
    @PostMapping("/api/v1/delivery/picked")
    public void getPickedDelivery(@NotBlank @RequestBody UUID deliveryId);

    // Эмуляция неудачного вручения товара
    @PostMapping("/api/v1/delivery/picked")
    void getFailedDelivery(@NotBlank @RequestBody UUID deliveryId);

    // Расчёт полной стоимости доставки заказа.
    @PostMapping("/api/v1/delivery/cost")
    Double getDeliveryCost(@Valid @RequestBody OrderDto orderDto);

}
