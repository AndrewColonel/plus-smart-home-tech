package ru.yandex.practicum.commerce.shopping.store.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.shopping.store.model.ProductCategory;
import ru.yandex.practicum.commerce.shopping.store.dal.dto.ProductDto;
import ru.yandex.practicum.commerce.shopping.store.dal.dto.SetProductQuantityRequest;

import java.util.List;
import java.util.UUID;

public interface StoreService {
    // Получение списка товаров по типу в пагинированном виде
    List<ProductDto> getAllProducts(ProductCategory productCategory, Pageable page);

    // Создание нового товара в ассортименте
    ProductDto createProduct(ProductDto productDto);

    // Обновление товара в ассортименте, например уточнение описания, характеристик и т.д.
    ProductDto updateProduct(ProductDto productDto);

    // Удалить товар из ассортимента магазина. Функция для менеджерского состава.
    boolean removeProduct(UUID productId);

    // Установка статуса по товару. API вызывается со стороны склада.
    boolean setStatusProduct(SetProductQuantityRequest request);

    // Получить сведения по товару из БД.
    ProductDto getProductById(UUID productId);

}
