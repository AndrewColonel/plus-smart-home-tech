package ru.yandex.practicum.commerce.shopping.cart.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.commerce.iteraction.api.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.iteraction.api.dto.warehouse.BookingProductsDto;
import ru.yandex.practicum.commerce.iteraction.api.exception.NoAuthorizedUserException;
import ru.yandex.practicum.commerce.iteraction.api.dto.common.ShoppingCartDto;
import ru.yandex.practicum.commerce.iteraction.api.feignclient.WarehouseClient;
import ru.yandex.practicum.commerce.shopping.cart.repository.ShoppingCartRepository;
import ru.yandex.practicum.commerce.shopping.cart.model.CartState;
import ru.yandex.practicum.commerce.shopping.cart.model.entity.UserCart;

import java.time.LocalDateTime;
import java.util.*;

import static ru.yandex.practicum.commerce.shopping.cart.model.ShoppingCartMapper.toDto;

@Service
@AllArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final ShoppingCartRepository repository;

    private final WarehouseClient client;

    @Override
    public ShoppingCartDto getUserCart(String username) {
        return toDto(getCartByUser(username));
    }

    @Override
    public ShoppingCartDto createUserCart(String username, Map<UUID, Integer> products) {
        Optional<UserCart> maybeUserCart = repository.findByUserName(username);
        // если корзина пользователя существет - обновим ее
        if (maybeUserCart.isPresent()) {
            UserCart userCart = maybeUserCart.get();

            // если статус корзины - активен, то можем обновить
            if (userCart.getCartState().equals(CartState.DEACTIVATED)) {
                return toDto(userCart);
            }
            userCart.setProducts(products);
            return toDto(repository.save(userCart));

        }
        // корзины еще не было, создаем с активным статусом и временем создания
        return toDto(repository.save(UserCart.builder()
                .userName(username)
                .createdAt(LocalDateTime.now())
                .products(products)
                .cartState(CartState.ACTIVE)
                .build()));
    }

    @Override
    public void deactivateUserCart(String username) {
        UserCart userCart = getCartByUser(username);
        userCart.setCartState(CartState.DEACTIVATED);
        repository.save(userCart);
    }

    @Override
    public ShoppingCartDto removeUserProducts(String username, List<UUID> productIds) {
        UserCart userCart = getCartByUser(username);
        // если статус корзины - активен, то можем удалять продукты
        if (userCart.getCartState().equals(CartState.DEACTIVATED)) {
            return toDto(userCart);
        }
        productIds.forEach(uuid -> {
            if (uuid != null)
                userCart.getProducts().remove(uuid);
        });

        return toDto(repository.save(userCart));
    }

    @Override
    public ShoppingCartDto updateUserCart(String username, ChangeProductQuantityRequest request) {
        UserCart userCart = getCartByUser(username);
        // если статус корзины - активен, то можем обновить
        if (userCart.getCartState().equals(CartState.DEACTIVATED)) {
            return toDto(userCart);
        }


//        userCart.setProducts(request.getProducts());

        userCart.getProducts().put(request.getProductId(), request.getNewQuantity());

        // проверим склад с помощью feign-client

            try {

                BookingProductsDto bookingProductsDto = client.check(toDto(userCart));
                log.info("Общие сведения по корзине {} о доставке {}", userCart.getCartId(), bookingProductsDto);

            } catch (FeignException e) {
                if (e.status() == 400) {
                    log.warn("для корзины {}, на складе не хватет товаров {}",
                            userCart.getCartId(), userCart.getProducts());
                }
            }
        return toDto(repository.save(userCart));
    }

    // вспомогаительные методы
    private UserCart getCartByUser(String username) {
        return repository.findByUserName(username).orElseThrow(
                () -> new NoAuthorizedUserException(
                        String.format("Пользователь %s не найден", username),
                        "Пользователь не найден",
                        HttpStatus.UNAUTHORIZED, new NoSuchElementException("Такого пользователя нет в базе")));
    }

}
