package ru.yandex.practicum.commerce.warehouse.service;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.ShoppingCartDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.*;
import ru.yandex.practicum.commerce.iteraction.api.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.iteraction.api.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.iteraction.api.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.iteraction.api.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.AddressDto;
import ru.yandex.practicum.commerce.iteraction.api.feign.clients.StoreClient;
import ru.yandex.practicum.commerce.warehouse.model.entity.OrderBooking;
import ru.yandex.practicum.commerce.warehouse.repository.OrderedItemsRepository;
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

    private final WarehouseRepository repository;
    private final OrderedItemsRepository orderedItemsRepository;

    private final StoreClient storeClient;

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

        repository.save(WarehouseMapper.toEntity(request));
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
        repository.save(item);
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
        OrderBooking booking = orderedItemsRepository.findByOrderId(request.getOrderId()).orElseThrow(
                () -> new NoOrderFoundException(
                        String.format(" Заказа с id %S нет в таблице квукИщщлштпы", request.getOrderId()),
                        "Нельзя обновить информацию о заказе",
                        HttpStatus.BAD_REQUEST, new NoSuchElementException("Нет информации о заказе на складе"))
        );
        booking.setDeliveryId(request.getDeliveryId());
        orderedItemsRepository.save(booking);

    }

    @Override
    public void returnProductsOrder(Map<UUID, Integer> products) {
        log.info("Возврат товаров {} на склад", products);
        warehouseProcessor(products,
                WarehouseItemState.RETURN, null);
    }

    @Override
    public BookingProductsDto assemblyProductsOrder(AssemblyProductsForOrderRequest request) {
        log.info("Сборка товаров {} для заказа {}", request.getProducts(), request.getOrderId());
        return warehouseProcessor(request.getProducts(),
                WarehouseItemState.ASEMBLY, request.getOrderId());
    }

    // вспомогательный метод
    private Optional<WarehouseItem> getWarehouseItem(UUID productId) {
        return repository.findByProductId(productId);
    }

    private BookingProductsDto warehouseProcessor(Map<UUID, Integer> products,
                                                  WarehouseItemState state, UUID uuid) {

        // Список id продуктов из корзины
        List<UUID> productIds = products.keySet().stream().toList();
        // мапа соответсвующих корзине позиций на складе
        Map<UUID, WarehouseItem> warehouseItems = repository.findByProductIdIn(productIds).stream()
                .collect(Collectors.toMap(
                        WarehouseItem::getProductId,
                        Function.identity()));
        // список и мапа для сбора подходящих позиций корзины и склада
        List<WarehouseItem> checkedWarehouseItems = new ArrayList<>();
        Map<UUID, Integer> deficitCartItems = new HashMap<>();
        // сравним номенклатуру склада и позиции из корзины
        for (Map.Entry<UUID, Integer> cartItem : products.entrySet()) {
            // если товар из корзины содержится на склада
            if (warehouseItems.containsKey(cartItem.getKey())) {
                WarehouseItem warehouseItem = warehouseItems.get(cartItem.getKey());

                if (Objects.nonNull(warehouseItem)) {
                    // если количество данного товара на склада достаточно для корзины
                    if (warehouseItem.getQuantity() >= cartItem.getValue()) {
                        // записываем этот товар в требуемом количестве в подготовленный список
                        warehouseItem.setQuantity(cartItem.getValue());
                        checkedWarehouseItems.add(warehouseItem);
                    } else {
                        // если товара нет на складе в необходимомо количестве,
                        // добавим его в соотвествкющую мапу с указанием недостающего кол-ва единиц тоавара
                        deficitCartItems.put(cartItem.getKey(), cartItem.getValue() - warehouseItem.getQuantity());
                    }
                }

            } else {
                // если товара нет на складе вообще, добавим его в соотвествкющую мапу
                deficitCartItems.entrySet().add(cartItem);
            }
        }

        // Ошибка, товар из корзины не находится в требуемом количестве на складе
        if (!deficitCartItems.isEmpty()) {
            log.trace("На складе не хватет следующих позиций {}", deficitCartItems);
            throw new ProductInShoppingCartLowQuantityInWarehouse(
                    String.format("Товаров id %S из корзины нет на складе ребуемом количестве", deficitCartItems.keySet()),
                    "товар из корзины не находится в требуемом количестве на складе",
                    HttpStatus.BAD_REQUEST, new NoSuchElementException("Нет информации о товаре на складе")
            );
        }

        // расчет остатка склада после сбора заказа или возврата товара на склад
        switch (state) {
            case ASEMBLY -> {
                for (WarehouseItem checkedItem : checkedWarehouseItems) {
                    // warehouseItems точно содержит не NULL объект warehouseItem из списка проверенных товаров
                    // уменьшаем количество товара на складе
                    WarehouseItem warehouseItem = warehouseItems.get(checkedItem.getProductId());
                    warehouseItem.setQuantity(warehouseItem.getQuantity() - checkedItem.getQuantity());

                    // формируем заказ OrderBooking - перекладываю checkedItem в таблицу заказа
                    if (Objects.nonNull(uuid)) {
                        orderedItemsRepository.save(OrderBooking.builder()
                                .orderId(uuid)
                                .productId(checkedItem.getProductId())
                                .quantity(checkedItem.getQuantity())
                                .build());
                    }

                }
                repository.saveAll(warehouseItems.values().stream().toList());
                log.trace("Выполнен расчет остатков склада после сбора заказа на следующие позиции {}",
                        products);
            }
            case RETURN -> {
                for (WarehouseItem checkedItem : checkedWarehouseItems) {
                    // warehouseItems точно содержит не NULL объект warehouseItem из списка проверенных товаров
                    WarehouseItem warehouseItem = warehouseItems.get(checkedItem.getProductId());
                    warehouseItem.setQuantity(warehouseItem.getQuantity() + checkedItem.getQuantity());
                }
                repository.saveAll(warehouseItems.values().stream().toList());
                log.trace("Выполнен расчет остатков склада после возврата заказа со следующими товарами{}",
                        products);
            }
            case CHECK -> {
                log.trace("Выполнена проверка остатков склада для товаров {}",
                        productIds);
            }
        }
        // на складе есть товары для корзины, расчитаем предварительные  параметры для доставки
        boolean fragile = false;
        double deliveryweight = 0.0;
        double deliveryvolume = 0.0;
        for (WarehouseItem warehouseItem : checkedWarehouseItems) {
            deliveryweight = deliveryweight + warehouseItem.getWeight();
            deliveryvolume = deliveryvolume
                    + (warehouseItem.getDimension().getDepth()
                    * warehouseItem.getDimension().getHeight()
                    * warehouseItem.getDimension().getWidth());
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
