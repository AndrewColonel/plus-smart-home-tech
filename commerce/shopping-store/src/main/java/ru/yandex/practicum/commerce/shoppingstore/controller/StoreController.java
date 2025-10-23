package ru.yandex.practicum.commerce.shoppingstore.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.shoppingstore.dal.dto.ProductDto;

import javax.validation.constraints.NotBlank;
import java.util.Collection;


@RestController
@Slf4j
@Validated
@RequestMapping("/api/v1/shopping-store")
@AllArgsConstructor
public class StoreController {

    @GetMapping
    public Collection<ProductDto> getAll(@RequestBody String categoryFromBody,
                                         @NotBlank @RequestParam String category,
                                         @NotBlank @RequestParam Pageable page) {
        log.debug(">>> StoreController: GET /api/v1/shopping-store");
        log.debug(">>> Запрос на просмотр устройств {}", categoryFromBody);
//        log.warn("ИТОГ: Список пользователей {}", );
        return null;
    }


//    @PutMapping
//
//
//    @PostMapping
//
//
//    @PostMapping("/removeProductFromStore")
//
//
//    @PostMapping("/removeProductFromStore")
//
//
//    @PostMapping("/quantityState")
//
//
//    @GetMapping("/{productId}")


}
