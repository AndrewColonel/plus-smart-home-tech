package ru.yandex.practicum.commerce.warehouse.service;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.ShoppingCartDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.*;

import ru.yandex.practicum.commerce.iteraction.api.exception.*;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.AddressDto;
import ru.yandex.practicum.commerce.warehouse.model.entity.OrderBooking;
import ru.yandex.practicum.commerce.warehouse.repository.OrderBookingRepository;
import ru.yandex.practicum.commerce.warehouse.repository.WarehouseRepository;
import ru.yandex.practicum.commerce.warehouse.model.WarehouseMapper;
import ru.yandex.practicum.commerce.warehouse.model.entity.WarehouseItem;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final OrderBookingRepository orderBookingRepository;

    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};

    private enum WarehouseItemState {RETURN, ASEMBLY, CHECK}

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    @Override
    public void createWarehouseItem(NewProductInWarehouseRequest request) {
        // Ошибка, товар с таким описанием уже зарегистрирован на складе
        if (getWarehouseItem(request.getProductId()).isPresent()) {
            throw new SpecifiedProductAlreadyInWarehouseException(
                    String.format("Товар с id %S уже зарегистрирован на складе", request.getProductId()),
                    "товар с таким описанием уже зарегистрирован на складе",
                    HttpStatus.BAD_REQUEST, new DuplicateRequestException("Товар уже в базе"));
        }
        warehouseRepository.save(WarehouseMapper.toEntity(request));
    }

    @Override
    public BookingProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto) {
        log.info("Проверка достаточного количества товаров для корзины {}",
                shoppingCartDto.getShoppingCartId());
        return warehouseProcessor(shoppingCartDto.getProducts(),
                WarehouseItemState.CHECK, null);

    }

    @Override
    public void addProductsToWarehouse(AddProductToWarehouseRequest request) {
        WarehouseItem item = getWarehouseItem(request.getProductId()).orElseThrow(() ->
                new NoSpecifiedProductInWarehouseException(
                        String.format("Товара с id %S нет на складе", request.getProductId()),
                        "Нельзя обновить информацию о товаре на складе",
                        HttpStatus.BAD_REQUEST, new NoSuchElementException("Нет информации о товаре на складе")));
        item.setQuantity(request.getQuantity());
        warehouseRepository.save(item);
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return AddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }

    @Override
    public void shippedToDeliveryOrder(ShippedToDeliveryRequest request) {
        OrderBooking orderBooking = getOrderBooking(request.getOrderId()).orElseThrow(
                () -> new NoOrderBookingFoundException(
                        String.format("Заказ %s не найден", request.getOrderId()),
                        "Заказ не найден",
                        HttpStatus.BAD_REQUEST, new NoSuchElementException("Такого Заказа нет в базе")));

        orderBooking.setDeliveryId(request.getDeliveryId());
        orderBookingRepository.save(orderBooking);
    }

    @Override
    public void returnProductsOrder(Map<UUID, Integer> products) {
        log.info("Возврат товаров {} на склад", products);
        // Список id продуктов для возврата
        List<UUID> productIds = products.keySet().stream().toList();
        // список соответсвующих возратных позиций со склада
        List<WarehouseItem> warehouseItems = getWarehouseItemList(productIds);
        for (WarehouseItem warehouseItem : warehouseItems) {
            Integer newQuantity = warehouseItem.getQuantity() + products.get(warehouseItem.getProductId());
            warehouseItem.setQuantity(newQuantity);
        }
        warehouseRepository.saveAll(warehouseItems);

    }

    @Override
    public BookingProductsDto assemblyProductsOrder(AssemblyProductsForOrderRequest request) {
        log.info("Сборка товаров {} для заказа {}", request.getProducts(), request.getOrderId());
        return warehouseProcessor(request.getProducts(),
                WarehouseItemState.ASEMBLY, request.getOrderId());
    }

    // вспомогательные методы
    private Optional<WarehouseItem> getWarehouseItem(UUID productId) {
        return warehouseRepository.findByProductId(productId);
    }

    private Optional<OrderBooking> getOrderBooking(UUID orderId) {
        return orderBookingRepository.findByOrderId(orderId);
    }

    private List<WarehouseItem> getWarehouseItemList(List<UUID> productIds) {
        List<WarehouseItem> warehouseItemList = warehouseRepository.findByProductIdIn(productIds);
        if (warehouseItemList.isEmpty()) {
            throw new NoSpecifiedProductInWarehouseException(
                    String.format("Позиций склада с id %S нет на складе", productIds),
                    "Нельзя обновить информацию о товаре на складе",
                    HttpStatus.BAD_REQUEST, new NoSuchElementException("Нет информации о товаре на складе"));
        }
        return warehouseItemList;
    }

    private BookingProductsDto warehouseProcessor(Map<UUID, Integer> products,
                                                  WarehouseItemState state, UUID orderId) {

        // Список id продуктов из корзины
        List<UUID> productIds = products.keySet().stream().toList();
        // мапа соответсвующих корзине позиций на складе
        Map<UUID, WarehouseItem> warehouseItems = getWarehouseItemList(productIds).stream()
                .collect(Collectors.toMap(
                        WarehouseItem::getProductId,
                        Function.identity()));
        // список и мапа для сбора подходящих позиций корзины и склада
        Map<UUID, Integer> checkedWarehouseItems = new HashMap<>();
        Map<UUID, Integer> deficitCartItems = new HashMap<>();
        // сравним номенклатуру склада и позиции из корзины
        for (Map.Entry<UUID, Integer> requestItem : products.entrySet()) {
            // если товар из корзины содержится на склада
            if (warehouseItems.containsKey(requestItem.getKey())) {
                WarehouseItem warehouseItem = warehouseItems.get(requestItem.getKey());

                if (Objects.nonNull(warehouseItem)) {
                    // если количество данного товара на склада достаточно для корзины
                    if (warehouseItem.getQuantity() >= requestItem.getValue()) {
                        // записываем эту позицию склада в подготовленную мапу "проверенных позиций"
                        checkedWarehouseItems.put(warehouseItem.getProductId(), requestItem.getValue());
                    } else {
                        // если товара нет на складе в необходимомо количестве,
                        // добавим его в соотвествкющую мапу с указанием недостающего кол-ва единиц тоавара
                        deficitCartItems.put(requestItem.getKey(), requestItem.getValue() - warehouseItem.getQuantity());
                    }
                }
            } else {
                // если товара нет на складе вообще, добавим его в соотвествкющую мапу
                deficitCartItems.entrySet().add(requestItem);
            }
        }

        // Ошибка, товар для корзины или заказа  не находится в требуемом количестве на складе
        if (!deficitCartItems.isEmpty()) {
            log.trace("На складе не хватет следующих позиций {}", deficitCartItems);
            throw new ProductInShoppingCartLowQuantityInWarehouse(
                    String.format("Товаров id %S из корзины нет на складе ребуемом количестве", deficitCartItems.keySet()),
                    "товар из корзины не находится в требуемом количестве на складе",
                    HttpStatus.BAD_REQUEST, new NoSuchElementException("Нет информации о товаре на складе")
            );
        }

        // на этом эьапе - на складе есть товары для корзины или заказа,
        // преобразую мапу в список проверенных  WarehouseItemдля дальнейших расчетов
        List<WarehouseItem> checkedWarehouseItemList = warehouseItems.values().stream()
                .filter(warehouseItem -> checkedWarehouseItems.containsKey(warehouseItem.getProductId()))
                .toList();

        // расчет остатка склада после сбора заказа или возврата товара на склад
        switch (state) {
            case ASEMBLY -> {
                // формируем запись для таблицы заказа, если такой заказ уже был - обновляем, нет - создаем
                if (Objects.nonNull(orderId)) {
                    Optional<OrderBooking> orderBooking = getOrderBooking(orderId);
                    if (orderBooking.isPresent()) {
                        orderBooking.get().setProducts(checkedWarehouseItems);
                    } else {
                        orderBookingRepository.save(OrderBooking.builder()
                                .orderId(orderId)
                                .products(checkedWarehouseItems)
                                .build());
                    }
                }
                for (WarehouseItem checkedItem : checkedWarehouseItemList) {
                    // warehouseItems точно содержит не NULL объект warehouseItem из списка проверенных товаров
                    // уменьшаем количество товара на складе
                    Integer newQuantity = checkedItem.getQuantity() - products.get(checkedItem.getProductId());

                    log.trace("----------------------");
                    log.trace("Товар: {}", checkedItem.getProductId());
                    log.trace("Было на складе: {}", checkedItem.getQuantity());
                    log.trace("Забрали для заказа: {}", products.get(checkedItem.getProductId()));
                    log.trace("Остаток на складе: {}", newQuantity);
                    log.trace("----------------------");

                    checkedItem.setQuantity(newQuantity);
                }
                warehouseRepository.saveAll(warehouseItems.values().stream().toList());
                log.trace("Выполнен расчет остатков склада после сбора заказа по следующим позициям {}",
                        products);
            }
            case CHECK -> {
                log.trace("Выполнена проверка остатков склада для товаров {}",
                        productIds);
            }
        }

        // расчитаем предварительные  параметры для доставки
        boolean fragile = false;
        double deliveryweight = 0.0;
        double deliveryvolume = 0.0;
        for (WarehouseItem warehouseItem : checkedWarehouseItemList) {
            deliveryweight = deliveryweight + warehouseItem.getWeight()
                    * checkedWarehouseItems.get(warehouseItem.getProductId());
            deliveryvolume = deliveryvolume
                    + (warehouseItem.getDimension().getDepth()
                    * warehouseItem.getDimension().getHeight()
                    * warehouseItem.getDimension().getWidth())
                    * checkedWarehouseItems.get(warehouseItem.getProductId());
            if (warehouseItem.getFragile().equals(true)) {
                // если один из товаров будет fragile-true, вся посылка будет такая же
                fragile = true;
            }
        }
        return BookingProductsDto.builder()
                .deliveryvolume(deliveryvolume)
                .deliveryweight(deliveryweight)
                .fragile(fragile)
                .build();
    }

}
