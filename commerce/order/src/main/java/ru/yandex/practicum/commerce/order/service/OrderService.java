package ru.yandex.practicum.commerce.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.ProductReturnRequest;

import java.util.UUID;

public interface OrderService {
    // 200 Список всех заказов пользователя (Точка улучшения и развития - пагинированный вывод)
    // 401 Имя пользователя не должно быть пустым
    Page<OrderDto> getAllUserOrders(String username, Pageable page);

    // 200 Оформленный заказ пользователя
    // 400 Нет заказываемого товара на складе
    OrderDto createOrder(CreateNewOrderRequest request);

    // 200 Заказ пользователя после сборки
    // 400 Не найден заказ
    OrderDto returnOrderRequest(ProductReturnRequest request);

    // 200 Заказ пользователя после оплаты
    // 400 Не найден заказ
    OrderDto orderPaymentRequest(UUID orderId);

    // 200 Заказ пользователя после ошибки оплаты
    // 400 Не найден заказ
    OrderDto orderPaymentFailedRequest(UUID orderId);

    // 200 Заказ пользователя после доставки
    // 400 Не найден заказ
    OrderDto orderDeliveryRequest(UUID orderId);

    // 200 Заказ пользователя после ошибки доставки
    // 400 Не найден заказ
    OrderDto orderDeliveryFailedRequest(UUID orderId);

    // 200 Заказ пользователя после всех стадий и завершенный
    // 400 Не найден заказ
    OrderDto orderComplitedRequest(UUID orderId);

    // 200 Заказ пользователя с расчётом общей стоимости
    // 400 Не найден заказ
    OrderDto orderCalculatedTotalRequest(UUID orderId);

    // 200 Заказ пользователя с расчётом доставки
    // 400 Не найден заказ
    OrderDto orderCalculatedDeliveryRequest(UUID orderId);

    // 200 Заказ пользователя после сборки
    // 400 Не найден заказ
    OrderDto orderAssemblyRequest(UUID orderId);

    // 200 Заказ пользователя после ошибки сборки
    // 400 Не найден заказ
    OrderDto orderAssemblyFailedRequest(UUID orderId);
}
