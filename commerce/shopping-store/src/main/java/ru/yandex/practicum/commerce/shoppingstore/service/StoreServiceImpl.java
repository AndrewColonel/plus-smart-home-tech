package ru.yandex.practicum.commerce.shoppingstore.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.shoppingstore.dal.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.dal.ProductMapper;
import ru.yandex.practicum.commerce.shoppingstore.dal.ProductState;
import ru.yandex.practicum.commerce.shoppingstore.dal.QuantityState;
import ru.yandex.practicum.commerce.shoppingstore.dal.dto.ProductDto;
import ru.yandex.practicum.commerce.shoppingstore.dal.dto.SetProductQuantityRequest;
import ru.yandex.practicum.commerce.shoppingstore.dal.entity.Product;
import ru.yandex.practicum.commerce.shoppingstore.dal.repository.ProductReposiitory;
import ru.yandex.practicum.commerce.shoppingstore.exception.NotFoundException;

import java.util.List;

import static ru.yandex.practicum.commerce.shoppingstore.dal.ProductMapper.toDto;
import static ru.yandex.practicum.commerce.shoppingstore.dal.ProductMapper.toEntity;

@Slf4j
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
        //TODO - механизм генерации productId?
        return toDto(reposiitory.save(toEntity(productDto)));
    }

    // Обновление товара в ассортименте, например уточнение описания, характеристик и т.д.
    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        getProduct(productDto.getProductId());
        return toDto(reposiitory.save(toEntity(productDto)));
    }

    // Удалить товар из ассортимента магазина. Функция для менеджерского состава.
    @Override
    public boolean removeProduct(String productId) {
        Product product = getProduct(productId);
        product.setProductState(ProductState.DEACTIVATE);
        return reposiitory.save(product).getProductState()
                .equals(ProductState.DEACTIVATE);
    }

    // Установка статуса по товару. API вызывается со стороны склада.
    @Override
    public boolean setStatusProduct(SetProductQuantityRequest request) {
        Product product = getProduct(request.getProductId());
        product.setQuantityState(QuantityState.valueOf(request.getQuantityState()));
        return reposiitory.save(product).getQuantityState().name()
                .equals(request.getQuantityState());
    }

    // Получить сведения по товару из БД.
    @Override
    public ProductDto getProductById(String productId) {
        return toDto(getProduct(productId));
    }

    // вспомогательный метод
    public Product getProduct(String productId) {
        return reposiitory.findByProductId(productId).orElseThrow(
                () -> new NotFoundException(String.format("Продукт с id %S не найден", productId)));
    }

}
