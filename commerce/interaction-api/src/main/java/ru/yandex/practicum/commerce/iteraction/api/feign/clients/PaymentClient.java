package ru.yandex.practicum.commerce.iteraction.api.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.iteraction.api.feign.fallback.PaymentClientFallBack;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@FeignClient(name = "payment", fallback = PaymentClientFallBack.class)
public interface PaymentClient {
    // Формирование оплаты для заказа (переход в платежный шлюз).
     @PostMapping("/api/v1/payment")
     PaymentDto getPayment(@Valid @RequestBody OrderDto orderDto);

    // Расчёт полной стоимости заказа.
       @PostMapping("/api/v1/payment/totalCost")
     Double getTotalCost(@Valid @RequestBody OrderDto orderDto);

    // Метод для эмуляции успешной оплаты в платежного шлюза.
        @PostMapping("/api/v1/payment/refund")
    public void getRefund(@NotBlank @RequestBody UUID paymentId);

    // Расчёт стоимости товаров в заказе.
      @PostMapping("/api/v1/payment/productCost")
     Double getProductCost(@Valid @RequestBody OrderDto orderDto);

    // Метод для эмуляции отказа в оплате платежного шлюза.
     @PostMapping("/api/v1/payment/failed")
     void getFailedRefund(@NotBlank @RequestBody UUID paymentId);
}
