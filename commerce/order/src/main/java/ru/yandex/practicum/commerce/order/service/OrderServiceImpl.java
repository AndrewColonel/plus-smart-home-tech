package ru.yandex.practicum.commerce.order.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.ShoppingCartDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.OrderState;
import ru.yandex.practicum.commerce.iteraction.api.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.BookingProductsDto;
import ru.yandex.practicum.commerce.iteraction.api.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.CartClient;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.DeliveryClient;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.PaymentClient;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.WarehouseClient;
import ru.yandex.practicum.commerce.order.model.OrderMapper;
import ru.yandex.practicum.commerce.order.model.entity.Order;
import ru.yandex.practicum.commerce.order.repository.OrderRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

import static ru.yandex.practicum.commerce.order.model.OrderMapper.toDto;
import static ru.yandex.practicum.commerce.order.model.OrderMapper.toEntity;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;
    private final WarehouseClient warehouseClient;
    private final CartClient cartClient;

    // 200 Список всех заказов пользователя (Точка улучшения и развития - пагинированный вывод)
    // 401 Имя пользователя не должно быть пустым
    @Override
    public Page<OrderDto> getAllUserOrders(String username, Pageable page) {
        ShoppingCartDto shoppingCartDto = cartClient.get(username);
        return repository.findAllByShoppingCartId(shoppingCartDto.getShoppingCartId(), page)
                .map(OrderMapper::toDto);
    }

    // 200 Созданный заказ пользователя
    // 400 Нет заказываемого товара на складе
    @Override
    public OrderDto createOrder(CreateNewOrderRequest request) {
        // создание заказа - проверка товаров по номеру корзины
        BookingProductsDto bookingProductsDto = warehouseClient.check(request.getShoppingCart());
        log.info("Общие сведения по корзине {} о доставке {}",
                request.getShoppingCart().getShoppingCartId(), bookingProductsDto);

        Order order = Order.builder()
                .shoppingCartId(request.getShoppingCart().getShoppingCartId())
                .products(request.getShoppingCart().getProducts())
                .state(OrderState.NEW)
                .deliveryWeight(bookingProductsDto.getDeliveryweight())
                .deliveryVolume(bookingProductsDto.getDeliveryvolume())
                .fragile(bookingProductsDto.getFragile())
                .deliveryAddress(toEntity(request.getDeliveryAddress()))
                .build();
        return toDto(repository.save(order));
    }

    // 200 Заказ пользователя после сборки
    // 400 Не найден заказ
    @Override
    public OrderDto returnOrderRequest(ProductReturnRequest request) {
        getOrderById(request.getOrderId());

        return null;
    }

    // 200 Заказ пользователя после оплаты
    // 400 Не найден заказ
    @Override
    public OrderDto orderPaymentRequest(UUID orderId) {
        getOrderById(orderId);

        return null;
    }

    // 200 Заказ пользователя после ошибки оплаты
    // 400 Не найден заказ
    @Override
    public OrderDto orderPaymentFailedRequest(UUID orderId) {
        getOrderById(orderId);

        return null;
    }

    // 200 Заказ пользователя после доставки
    // 400 Не найден заказ
    @Override
    public OrderDto orderDeliveryRequest(UUID orderId) {
        getOrderById(orderId);

        return null;
    }

    // 200 Заказ пользователя после ошибки доставки
    // 400 Не найден заказ
    @Override
    public OrderDto orderDeliveryFailedRequest(UUID orderId) {
        getOrderById(orderId);

        return null;
    }

    // 200 Заказ пользователя после всех стадий и завершенный
    // 400 Не найден заказ
    @Override
    public OrderDto orderComplitedRequest(UUID orderId) {
        getOrderById(orderId);

        return null;
    }

    // 200 Заказ пользователя с расчётом общей стоимости
    // 400 Не найден заказ
    @Override
    public OrderDto orderCalculatedTotalRequest(UUID orderId) {
        getOrderById(orderId);

        return null;
    }

    // 200 Заказ пользователя с расчётом доставки
    // 400 Не найден заказ
    @Override
    public OrderDto orderCalculatedDeliveryRequest(UUID orderId) {
        getOrderById(orderId);

        return null;
    }

    // 200 Заказ пользователя после сборки
    // 400 Не найден заказ
    @Override
    public OrderDto orderAssemblyRequest(UUID orderId) {
        getOrderById(orderId);

        return null;
    }

    // 200 Заказ пользователя после ошибки сборки
    // 400 Не найден заказ
    @Override
    public OrderDto orderAssemblyFailedRequest(UUID orderId) {
        getOrderById(orderId);

        return null;
    }

    // вспомогаительные методы
    private Order getOrderById(UUID orderId) {
        return repository.findById(orderId).orElseThrow(
                () -> new NoOrderFoundException(
                        String.format("Заказ %s не найден", orderId),
                        "400 Не найден заказ",
                        HttpStatus.NOT_FOUND, new NoSuchElementException("Такого Заказа нет в базе")));
    }

}
