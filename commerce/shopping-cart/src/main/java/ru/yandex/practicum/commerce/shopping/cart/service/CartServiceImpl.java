package ru.yandex.practicum.commerce.shopping.cart.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.commerce.iteraction.api.exception.NoAuthorizedUserException;
import ru.yandex.practicum.commerce.iteraction.api.exception.NoProductsInCartException;
import ru.yandex.practicum.commerce.shopping.cart.dal.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.shopping.cart.dal.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.shopping.cart.dal.repository.ShoppingCartRepository;
import ru.yandex.practicum.commerce.shopping.cart.model.entity.UserCart;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static ru.yandex.practicum.commerce.shopping.cart.model.ShoppingCartMapper.toDto;

@Service
@AllArgsConstructor
public class CartServiceImpl {

    private final ShoppingCartRepository repository;

    public ShoppingCartDto getUserCart(String username) {
        return toDto(getCartByUser(username));
    }

    public ShoppingCartDto createUserCart(String username, Map<UUID, Integer> products) {
        Optional<UserCart> maybeUserCart = repository.findByUserName(username);
        if (maybeUserCart.isPresent()) {
            UserCart userCart = maybeUserCart.get();
            userCart.setProducts(products);
            return toDto(repository.save(userCart));
        }
        return toDto(repository.save(UserCart.builder()
                .userName(username)
                .createdAt(LocalDateTime.now())
                .products(products)
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
                    (k, oldQuantity) ->
                            (oldQuantity == null) ? requestNewQuantity : requestNewQuantity + oldQuantity);

        } else {
            throw new NoProductsInCartException(
                    String.format("Пролукт с id %s не найден", requestProductId),
                    "Продукт не найден",
                    HttpStatus.BAD_REQUEST, new NoSuchElementException("Такого продукта нет в базе"));
            // cartProducts.put(requestProductId, requestNewQuantity);
        }
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
