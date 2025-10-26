package ru.yandex.practicum.commerce.shopping.store.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.shopping.store.model.ProductCategory;
import ru.yandex.practicum.commerce.shopping.store.dal.dto.ProductDto;
import ru.yandex.practicum.commerce.shopping.store.dal.dto.SetProductQuantityRequest;
import ru.yandex.practicum.commerce.shopping.store.service.StoreService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.UUID;


@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1/shopping-store")
@AllArgsConstructor
public class StoreController {

    private final StoreService service;

    @GetMapping
    public Collection<ProductDto> getAll(
            //TODO - непонятно зачем в теле выдавать категорию
//            @RequestBody ProductCategory productCategory,
            @NotBlank @RequestParam ProductCategory category,
            @PageableDefault(size = 20) Pageable page) {
        log.debug(">>> StoreController: GET /api/v1/shopping-store");
//        log.debug(">>> Запрос на просмотр устройств {}", categoryFromBody);
//        log.warn("ИТОГ: Список пользователей {}", );
        return service.getAllProducts(category, page);
    }


    // TODO добавить логи

    @PutMapping
    public ProductDto create(@Valid @RequestBody ProductDto productDto) {
        return service.createProduct(productDto);
    }

    @PostMapping
    public ProductDto update(@Valid @RequestBody ProductDto productDto) {
        return service.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public boolean remove(@NotBlank @RequestBody UUID productId) {
        return service.removeProduct(productId);
    }

    @PostMapping("/quantityState")
    public boolean setStatus(
            //TODO - по заданию в теле, на самом деле в тестах - в запросе
//            @RequestBody SetProductQuantityRequest productQuantityRequest,
            @Valid @ModelAttribute SetProductQuantityRequest request) {
        return service.setStatusProduct(request);
    }


    @GetMapping("/{productId}")
    public ProductDto getById(
            //TODO - непонятно зачем в теле дулировать параметры
//            @RequestBody UUID Id,
            @NotBlank @PathVariable("productId") UUID productId) {
        return service.getProductById(productId);
    }

}
