package ru.yandex.practicum.commerce.shopping.cart.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.commerce.iteraction.api.exception.NoAuthorizedUserException;
import ru.yandex.practicum.commerce.shopping.cart.dal.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.shopping.cart.dal.repository.ShoppingCartRepository;
import ru.yandex.practicum.commerce.shopping.cart.model.entity.Cart;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.yandex.practicum.commerce.shopping.cart.model.ShoppingCartMapper.toDto;

@Service
@AllArgsConstructor
public class CartServiceImpl {

    private final ShoppingCartRepository repository;

    public ShoppingCartDto getCart(String username) {
        return toDto(getCartByUser(username));
    }

    public ShoppingCartDto createCart(String username, Map<String, Integer> products) {
        return toDto(repository.save(Cart.builder()
                .userName(username)
                .products(products.entrySet().stream()
                        .collect(Collectors.toMap(
                                entry -> UUID.fromString(entry.getKey()),
                                Map.Entry::getValue)))

                .build()));
    }

    public void deleteCart() {


    }

    public ShoppingCartDto removeCart() {

        return null;
    }

    public ShoppingCartDto updateCart() {

        return null;
    }


    private Cart getCartByUser(String username) {
        return repository.findByUserName(username).orElseThrow(
                () -> new NoAuthorizedUserException(
                        String.format("Пользователь %s не найден", username),
                        "Пользователь не найден",
                        HttpStatus.UNAUTHORIZED, new NoSuchElementException("Такого пользователя нет в базе")));
    }

}
