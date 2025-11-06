package ru.yandex.practicum.commerce.order.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.order.repository.OrderRepository;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    // 200 Список всех заказов пользователя (Точка улучшения и развития - пагинированный вывод)
    // 401 Имя пользователя не должно быть пустым
    @Override
    public Page<OrderDto> getAllUserOrders(String username, Pageable page) {

        return null;
    }

    // 200 Оформленный заказ пользователя
    // 400 Нет заказываемого товара на складе
    @Override
    public OrderDto createOrder(CreateNewOrderRequest request) {

        return null;
    }

    // 200 Заказ пользователя после сборки
    // 400 Не найден заказ
    @Override
    public OrderDto returnOrderRequest(ProductReturnRequest request) {

        return  null;
    }

    // 200 Заказ пользователя после оплаты
    // 400 Не найден заказ
    @Override
    public OrderDto orderPaymentRequest(UUID orderId) {

        return null;
    }

    // 200 Заказ пользователя после ошибки оплаты
    // 400 Не найден заказ
    @Override
    public OrderDto orderPaymentFailedRequest(UUID orderId) {

        return null;
    }

    // 200 Заказ пользователя после доставки
    // 400 Не найден заказ
    @Override
    public OrderDto orderDeliveryRequest(UUID orderId) {

        return null;
    }

    // 200 Заказ пользователя после ошибки доставки
    // 400 Не найден заказ
    @Override
    public OrderDto orderDeliveryFailedRequest(UUID orderId) {

        return null;
    }

    // 200 Заказ пользователя после всех стадий и завершенный
    // 400 Не найден заказ
    @Override
    public OrderDto orderComplitedRequest(UUID orderId) {

        return null;
    }

    // 200 Заказ пользователя с расчётом общей стоимости
    // 400 Не найден заказ
    @Override
    public OrderDto orderCalculatedTotalRequest(UUID orderId) {

        return null;
    }

    // 200 Заказ пользователя с расчётом доставки
    // 400 Не найден заказ
    @Override
    public OrderDto orderCalculatedDeliveryRequest(UUID orderId) {

        return null;
    }

    // 200 Заказ пользователя после сборки
    // 400 Не найден заказ
    @Override
    public OrderDto orderAssemblyRequest(UUID orderId) {

        return null;
    }

    // 200 Заказ пользователя после ошибки сборки
    // 400 Не найден заказ
    @Override
    public OrderDto orderAssemblyFailedRequest(UUID orderId) {

        return null;
    }

}
