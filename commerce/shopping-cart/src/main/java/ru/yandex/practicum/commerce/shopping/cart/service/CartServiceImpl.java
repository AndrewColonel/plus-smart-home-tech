package ru.yandex.practicum.commerce.shopping.cart.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.commerce.iteraction.api.exception.NoAuthorizedUserException;
import ru.yandex.practicum.commerce.shopping.cart.dal.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.shopping.cart.dal.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.shopping.cart.dal.repository.ShoppingCartRepository;
import ru.yandex.practicum.commerce.shopping.cart.model.entity.UserCart;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import static ru.yandex.practicum.commerce.shopping.cart.model.ShoppingCartMapper.toDto;

@Service
@AllArgsConstructor
public class CartServiceImpl {

    private final ShoppingCartRepository repository;

    public ShoppingCartDto getUserCart(String username) {
        return toDto(getCartByUser(username));
    }

    public ShoppingCartDto createUserCart(String username) {
        return toDto(repository.save(UserCart.builder()
                .userName(username)
                .createdAt(LocalDateTime.now())
                .products(new HashMap<>())
                .build()));


    }

    public void deleteUserCart() {


    }

    public ShoppingCartDto removeUserCart() {

        return null;
    }

    public ShoppingCartDto updateUserCart(String username, ChangeProductQuantityRequest request) {
        UserCart userCart = getCartByUser(username);
        Map<UUID, Integer> cartProducts = userCart.getProducts();
        UUID requestProductId = UUID.fromString(request.getProductId());
        Integer requestNewQuantity = request.getNewQuantity();
        if (cartProducts.containsKey(requestProductId)) {
            cartProducts.compute(requestProductId,
                    (k, oldQuantity) -> oldQuantity + requestNewQuantity);
        } else {
            cartProducts.put(requestProductId,requestNewQuantity);
        }

        userCart.setProducts(cartProducts);

        return toDto(repository.save(userCart));
    }


    private UserCart getCartByUser(String username) {
        return repository.findByUserName(username).orElseThrow(
                () -> new NoAuthorizedUserException(
                        String.format("Пользователь %s не найден", username),
                        "Пользователь не найден",
                        HttpStatus.UNAUTHORIZED, new NoSuchElementException("Такого пользователя нет в базе")));
    }

}
