package ru.yandex.practicum.commerce.shopping.store.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.commerce.iteraction.api.logging.Loggable;
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

    @Loggable
    @GetMapping
    public Collection<ProductDto> getAll(
//            @RequestBody ProductCategory productCategory,
            @NotBlank @RequestParam ProductCategory category,
            @PageableDefault(size = 20) Pageable page) {
        return service.getAllProducts(category, page);

    }

    @Loggable
    @PutMapping
    public ProductDto create(@Valid @RequestBody ProductDto productDto) {
        return service.createProduct(productDto);
    }

    @Loggable
    @PostMapping
    public ProductDto update(@Valid @RequestBody ProductDto productDto) {
        return service.updateProduct(productDto);
    }

    @Loggable
    @PostMapping("/removeProductFromStore")
    public boolean remove(@NotBlank @RequestBody UUID productId) {
        return service.removeProduct(productId);
    }

    @Loggable
    @PostMapping("/quantityState")
    public boolean setStatus(
//            @RequestBody SetProductQuantityRequest productQuantityRequest,
            @Valid @ModelAttribute SetProductQuantityRequest request) {
        return service.setStatusProduct(request);
    }

    @Loggable
    @GetMapping("/{productId}")
    public ProductDto getById(
//            @RequestBody UUID Id,
            @NotBlank @PathVariable("productId") UUID productId) {
        return service.getProductById(productId);
    }

}
