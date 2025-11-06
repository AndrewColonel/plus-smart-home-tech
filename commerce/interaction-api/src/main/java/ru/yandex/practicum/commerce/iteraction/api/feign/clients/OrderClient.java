package ru.yandex.practicum.commerce.iteraction.api.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.ProductReturnRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@FeignClient(name = "order")
public interface OrderClient {
    // Получить заказы пользователя.
    @GetMapping("/api/v1/order")
    Page<OrderDto> getAll(@NotBlank @RequestParam String username,
                          @PageableDefault(size = 20) Pageable page);

    // Создать новый заказ в системе.
    @PutMapping("/api/v1/order")
    OrderDto create(@Valid @RequestBody CreateNewOrderRequest request);

    // Возврат заказа.
    @PostMapping("/api/v1/order/return")
    OrderDto returnRequest(@Valid @ModelAttribute ProductReturnRequest request);

    // Оплата заказа.
    @PostMapping("/api/v1/order/payment")
    OrderDto orderPayment(@NotBlank @RequestBody UUID orderId);

    // Оплата заказа произошла с ошибкой.
    @PostMapping("/api/v1/order/payment/failed")
    OrderDto orderPaymentFailed(@NotBlank @RequestBody UUID orderId);

    // Доставка заказа.
    @PostMapping("/api/v1/order/delivery")
    OrderDto orderDelivery(@NotBlank @RequestBody UUID orderId);

    // Доставка заказа произошла с ошибкой.
    @PostMapping("/api/v1/order/delivery/failed")
    public OrderDto orderDeliveryFailed(@NotBlank @RequestBody UUID orderId);

    // Завершение заказа.
    @PostMapping("/api/v1/order/completed")
    OrderDto orderCompleted(@NotBlank @RequestBody UUID orderId);

    // Расчёт стоимости заказа.
    @PostMapping("/api/v1/order/calculate/total")
    OrderDto orderCalculatedTotal(@NotBlank @RequestBody UUID orderId);

    // Расчёт стоимости доставки заказа.
    @PostMapping("/api/v1/order/calculate/delivery")
    OrderDto orderCalculatedDelivery(@NotBlank @RequestBody UUID orderId);

    // Сборка заказа.
    @PostMapping("/api/v1/order/assembly")
    OrderDto orderAssembly(@NotBlank @RequestBody UUID orderId);

    // Сборка заказа произошла с ошибкой.
    @PostMapping("/api/v1/order/assembly/failed")
    OrderDto orderAssemblyFailed(@NotBlank @RequestBody UUID orderId);
}
