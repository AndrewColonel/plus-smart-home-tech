package ru.yandex.practicum.commerce.iteraction.api.feign.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.iteraction.api.dto.store.ProductCategory;
import ru.yandex.practicum.commerce.iteraction.api.dto.store.ProductDto;
import ru.yandex.practicum.commerce.iteraction.api.dto.store.SetProductQuantityRequest;
import ru.yandex.practicum.commerce.iteraction.api.feign.fallback.StoreClentFallBack;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@FeignClient(name = "shopping-store", fallback = StoreClentFallBack.class)
public interface StoreClient {

    @GetMapping("/api/v1/shopping-store")
    Page<ProductDto> getAll(
            @NotBlank @RequestParam ProductCategory category,
            @PageableDefault(size = 20) Pageable page);

    @PutMapping("/api/v1/shopping-store")
    ProductDto create(@Valid @RequestBody ProductDto productDto);

    @PostMapping("/api/v1/shopping-store")
    ProductDto update(@Valid @RequestBody ProductDto productDto);

    @PostMapping("/api/v1/shopping-store/removeProductFromStore")
    boolean remove(@NotBlank @RequestBody UUID productId);

    @PostMapping("/api/v1/shopping-store/quantityState")
    boolean setStatus(
            @Valid @ModelAttribute SetProductQuantityRequest request);

    @GetMapping("/api/v1/shopping-store/{productId}")
    ProductDto getById(
            @NotBlank @PathVariable("productId") UUID productId);
}
