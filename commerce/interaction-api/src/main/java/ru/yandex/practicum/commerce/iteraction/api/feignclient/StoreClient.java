package ru.yandex.practicum.commerce.iteraction.api.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.iteraction.api.dto.store.ProductCategory;
import ru.yandex.practicum.commerce.iteraction.api.dto.store.ProductDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.store.SetProductQuantityRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@FeignClient(name = "shopping-store")
public interface StoreClient {

    @GetMapping
    Page<ProductDto> getAll(
            @NotBlank @RequestParam ProductCategory category,
            @PageableDefault(size = 20) Pageable page);

    @PutMapping
    ProductDto create(@Valid @RequestBody ProductDto productDto);

    @PostMapping
    ProductDto update(@Valid @RequestBody ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    boolean remove(@NotBlank @RequestBody UUID productId);

    @PostMapping("/quantityState")
    boolean setStatus(
            @Valid @ModelAttribute SetProductQuantityRequest request);

    @GetMapping("/{productId}")
    ProductDto getById(
            @NotBlank @PathVariable("productId") UUID productId);
}
