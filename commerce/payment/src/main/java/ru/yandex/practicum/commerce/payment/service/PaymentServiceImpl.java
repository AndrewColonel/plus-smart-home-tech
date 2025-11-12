package ru.yandex.practicum.commerce.payment.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.payment.PaymentState;
import ru.yandex.practicum.commerce.iteraction.api.dto.store.ProductDto;
import ru.yandex.practicum.commerce.iteraction.api.exception.NoPaymentFoundException;
import ru.yandex.practicum.commerce.iteraction.api.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.OrderClient;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.StoreClient;
import ru.yandex.practicum.commerce.payment.model.entity.Payment;
import ru.yandex.practicum.commerce.payment.repository.PaymentRepository;

import java.util.*;

import static ru.yandex.practicum.commerce.payment.model.PaymentMapper.toDto;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;

    private final StoreClient storeClient;
    private final OrderClient orderClient;

    // 200 Сформированная оплата заказа (переход в платежный шлюз)
    // 400 Недостаточно информации в заказе для расчёта
    @Override
    public PaymentDto getOrderPayment(OrderDto orderDto) {
        notEnoughInfoCheck(orderDto);

        return toDto(repository.save(Payment.builder()
                .orderId(orderDto.getOrderId())
                .deliveryId(orderDto.getDeliveryId())
                .totalPayment(getOrderProductCost(orderDto))
                .deliveryTotal(orderDto.getDeliveryPrice())
                .feeTotal(getOrderTotalCost(orderDto))
                .paymentState(PaymentState.PENDING)
                .build()));
    }

    // 200 Полная стоимость заказа
    // 400 Недостаточно информации в заказе для расчёта
    @Override
    public Double getOrderTotalCost(OrderDto orderDto) {
        notEnoughInfoCheck(orderDto);
        // От суммы стоимости всех товаров нужно взять 10% — это будет НДС
        double productCostAndTax = getOrderProductCost(orderDto) * 1.1;
        // плюс стоимость доставки
        return productCostAndTax + orderDto.getDeliveryPrice();
    }

    // 404 Заказ не найден
    @Override
    public void getOrderRefund(UUID paymentId) {
        // найти и проверить, что идентификатор оплаты существует
        Payment payment = getPaymentById(paymentId);
        // изменить статус на SUCCESS;
        payment.setPaymentState(PaymentState.SUCCESS);
        repository.save(payment);
        // вызвать изменение в сервисе заказов — статус оплачен.
        orderClient.orderPayment(payment.getOrderId());
    }

    // 200 Расчёт стоимости товаров в заказе
    // 400 Недостаточно информации в заказе для расчёта
    @Override
    public Double getOrderProductCost(OrderDto orderDto) {
        notEnoughInfoCheck(orderDto);
        Map<UUID, Integer> products = orderDto.getProducts();
        List<ProductDto> productDtoList = storeClient.getList(
                products.keySet().stream().toList());
        double productCost = 0.0;
        if (!productDtoList.isEmpty()) {
            for (ProductDto productDto : productDtoList) {
                // расчитывем общую стоимость товаров - цена из store, кол-во из заказа
                productCost = productCost
                        + productDto.getPrice() * products.get(productDto.getProductId());
            }
        }
        return productCost;
    }

    // 404 Заказ не найден
    @Override
    public void getOrderFailedRefund(UUID paymentId) {
        // найти и проверить, что идентификатор оплаты существует
        Payment payment = getPaymentById(paymentId);
        // изменить статус на FAILED;
        payment.setPaymentState(PaymentState.FAILED);
        repository.save(payment);
        // вызвать изменение в сервисе заказов — статус не оплачен.
        orderClient.orderPaymentFailed(payment.getOrderId());
    }

    // вспомогательные методы
    private void notEnoughInfoCheck(OrderDto orderDto) {
        if (Objects.isNull(orderDto.getDeliveryPrice())) {
            throw new NotEnoughInfoInOrderToCalculateException(
                    String.format("В заказе с id %S нет данных о стоимости доставки", orderDto.getOrderId()),
                    "Недостаточно информации в заказе для расчёта",
                    HttpStatus.BAD_REQUEST, new NoSuchElementException("данных по оплате доставки нет в базе"));
        }
        if (orderDto.getProducts().isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException(
                    String.format("В заказе с id %S нет информации по списку продуктов", orderDto.getOrderId()),
                    "Недостаточно информации в заказе для расчёта",
                    HttpStatus.BAD_REQUEST, new NoSuchElementException("Такого Заказа нет в базе"));
        }
    }

    private Payment getPaymentById(UUID paymentId) {
        return repository.findById(paymentId).orElseThrow(() ->
                new NoPaymentFoundException(
                        String.format("Оплата с id %S не найдена", paymentId),
                        "Нет информации об оплатне",
                        HttpStatus.NOT_FOUND, new NoSuchElementException("данных по оплате доставки нет в базе")));
    }
}
