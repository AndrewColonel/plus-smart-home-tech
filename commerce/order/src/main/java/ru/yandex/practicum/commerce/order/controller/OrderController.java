package ru.yandex.practicum.commerce.order.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.iteraction.api.logging.Loggable;
import ru.yandex.practicum.commerce.order.service.OrderService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
@Validated
public class OrderController {

    final private OrderService service;

    // Получить заказы пользователя.
    @Loggable
    @GetMapping
    public Page<OrderDto> getAll(@NotBlank @RequestParam String username,
                                 @PageableDefault(size = 20) Pageable page) {
        return service.getAllUserOrders(username, page);
    }

    // Создать новый заказ в системе.
    @Loggable
    @PutMapping
    public OrderDto create(@Valid @RequestBody CreateNewOrderRequest request) {
        return service.createOrder(request);
    }

    // Возврат заказа.
    @Loggable
    @PostMapping("/return")
    public OrderDto returnRequest(@Valid @ModelAttribute ProductReturnRequest request) {
        return service.returnOrderRequest(request);
    }

    // Оплата заказа.
    @Loggable
    @PostMapping("/payment")
    public OrderDto orderPayment(@NotBlank @RequestBody UUID orderId) {
        return service.orderPaymentRequest(orderId);
    }

    // Оплата заказа произошла с ошибкой.
    @Loggable
    @PostMapping("/payment/failed")
    public OrderDto orderPaymentFailed(@NotBlank @RequestBody UUID orderId) {
        return service.orderPaymentFailedRequest(orderId);
    }

    // Доставка заказа.
    @Loggable
    @PostMapping("/delivery")
    public OrderDto orderDelivery(@NotBlank @RequestBody UUID orderId) {
        return service.orderDeliveryRequest(orderId);
    }

    // Доставка заказа произошла с ошибкой.
    @Loggable
    @PostMapping("/delivery/failed")
    public OrderDto orderDeliveryFailed(@NotBlank @RequestBody UUID orderId) {
        return service.orderDeliveryFailedRequest(orderId);
    }

    // Завершение заказа.
    @Loggable
    @PostMapping("/completed")
    public OrderDto orderCompleted(@NotBlank @RequestBody UUID orderId) {
        return service.orderComplitedRequest(orderId);
    }

    // Расчёт стоимости заказа.
    @Loggable
    @PostMapping("/calculate/total")
    public OrderDto orderCalculatedTotal(@NotBlank @RequestBody UUID orderId) {
        return service.orderCalculatedTotalRequest(orderId);
    }

    // Расчёт стоимости доставки заказа.
    @Loggable
    @PostMapping("/calculate/delivery")
    public OrderDto orderCalculatedDelivery(@NotBlank @RequestBody UUID orderId) {
        return service.orderCalculatedDeliveryRequest(orderId);
    }

    // Сборка заказа.
    @Loggable
    @PostMapping("/assembly")
    public OrderDto orderAssembly(@NotBlank @RequestBody UUID orderId) {
        return service.orderAssemblyRequest(orderId);
    }

    // Сборка заказа произошла с ошибкой.
    @Loggable
    @PostMapping("/assembly/failed")
    public OrderDto orderAssemblyFailed(@NotBlank @RequestBody UUID orderId) {
        return service.orderAssemblyFailedRequest(orderId);
    }

}
