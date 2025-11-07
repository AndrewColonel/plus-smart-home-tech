package ru.yandex.practicum.commerce.payment.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.iteraction.api.logging.Loggable;
import ru.yandex.practicum.commerce.payment.service.PaymentService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/payment")
@Validated
public class PaymentController {

    private final PaymentService service;

    // Формирование оплаты для заказа (переход в платежный шлюз).
    @Loggable
    @PostMapping
    public PaymentDto getPayment(@Valid @RequestBody OrderDto orderDto) {
        return service.getOrderPayment(orderDto);
    }

    // Расчёт полной стоимости заказа.
    @Loggable
    @PostMapping("/totalCost")
    public Double getTotalCost(@Valid @RequestBody OrderDto orderDto) {
        return service.getOrderTotalCost(orderDto);
    }

    // Метод для эмуляции успешной оплаты в платежного шлюза.
    @Loggable
    @PostMapping("/refund")
    public void getRefund(@NotBlank @RequestBody UUID paymentId) {
        service.getOrderRefund(paymentId);
    }

    // Расчёт стоимости товаров в заказе.
    @Loggable
    @PostMapping("/productCost")
    public Double getProductCost(@Valid @RequestBody OrderDto orderDto) {
        return service.getOrderProductCost(orderDto);
    }

    // Метод для эмуляции отказа в оплате платежного шлюза.
    @Loggable
    @PostMapping("/failed")
    public void getFailedRefund(@NotBlank @RequestBody UUID paymentId) {
        service.getOrderFailedRefund(paymentId);
    }

}
