package ru.yandex.practicum.commerce.shoppingstore.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.shoppingstore.dal.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.dal.dto.Page;
import ru.yandex.practicum.commerce.shoppingstore.dal.dto.ProductDto;
import ru.yandex.practicum.commerce.shoppingstore.dal.dto.SetProductQuantityRequest;
import ru.yandex.practicum.commerce.shoppingstore.service.StoreService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Collection;


@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1/shopping-store")
@AllArgsConstructor
public class StoreController {

    private final StoreService service;

    @GetMapping
    public Collection<ProductDto> getAll(
//            @RequestBody String categoryFromBody,
            @NotBlank @RequestParam ProductCategory category,
            @Valid @ModelAttribute Page page) {
        log.debug(">>> StoreController: GET /api/v1/shopping-store");
//        log.debug(">>> Запрос на просмотр устройств {}", categoryFromBody);
//        log.warn("ИТОГ: Список пользователей {}", );
        return service.getAllProducts(category, page.toPageable());
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
    public boolean remove(@NotBlank @RequestBody String productId) {
        return service.removeProduct(productId);
    }

    @PostMapping("/quantityState")
    public boolean setStatus(@Valid @RequestBody SetProductQuantityRequest request) {
        return service.setStatusProduct(request);
    }


    @GetMapping("/{productId}")
    public ProductDto getById(@NotBlank @PathVariable("productId") String productId ) {
        return service.getProductById(productId);
    }

}
