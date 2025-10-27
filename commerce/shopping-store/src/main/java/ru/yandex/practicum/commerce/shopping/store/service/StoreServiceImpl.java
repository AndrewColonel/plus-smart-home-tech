package ru.yandex.practicum.commerce.shopping.store.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.iteraction.api.exception.NotFoundException;
import ru.yandex.practicum.commerce.shopping.store.model.ProductCategory;
import ru.yandex.practicum.commerce.shopping.store.model.ProductMapper;
import ru.yandex.practicum.commerce.shopping.store.model.ProductState;
import ru.yandex.practicum.commerce.shopping.store.dal.dto.ProductDto;
import ru.yandex.practicum.commerce.shopping.store.dal.dto.SetProductQuantityRequest;
import ru.yandex.practicum.commerce.shopping.store.model.entity.Product;
import ru.yandex.practicum.commerce.shopping.store.dal.repository.ProductReposiitory;


import java.util.List;
import java.util.UUID;

import static ru.yandex.practicum.commerce.shopping.store.model.ProductMapper.toDto;
import static ru.yandex.practicum.commerce.shopping.store.model.ProductMapper.toEntity;

@Service
@AllArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final ProductReposiitory reposiitory;

    // Получение списка товаров по типу в пагинированном виде
    @Override
    public List<ProductDto> getAllProducts(ProductCategory productCategory, Pageable page) {
        return reposiitory.findAllByProductCategory(productCategory, page).stream()
                .map(ProductMapper::toDto)
                .toList();
    }

    // Создание нового товара в ассортименте
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        return toDto(reposiitory.save(toEntity(productDto)));
    }

    // Обновление товара в ассортименте, например уточнение описания, характеристик и т.д.
    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        getProduct(UUID.fromString(productDto.getProductId()));
        return toDto(reposiitory.save(toEntity(productDto)));
    }

    // Удалить товар из ассортимента магазина. Функция для менеджерского состава.
    @Override
    public boolean removeProduct(UUID productId) {
        Product product = getProduct(productId);
        product.setProductState(ProductState.DEACTIVATE);
        return reposiitory.save(product).getProductState()
                .equals(ProductState.DEACTIVATE);
    }

    // Установка статуса по товару. API вызывается со стороны склада.
    @Override
    public boolean setStatusProduct(SetProductQuantityRequest request) {
        Product product = getProduct(UUID.fromString(request.getProductId()));
        product.setQuantityState(request.getQuantityState());
        return reposiitory.save(product).getQuantityState()
                .equals(request.getQuantityState());
    }

    // Получить сведения по товару из БД.
    @Override
    public ProductDto getProductById(UUID productId) {
        return toDto(getProduct(productId));
    }

    // вспомогательный метод
    public Product getProduct(UUID productId) {
        return reposiitory.findByProductId(productId).orElseThrow(
                () -> new NotFoundException(String.format("Продукт с id %S не найден", productId),
                        "Продукт не найден",
                        HttpStatus.NOT_FOUND));
    }

}
