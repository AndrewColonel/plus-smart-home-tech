package ru.yandex.practicum.commerce.shoppingstore.dal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import ru.yandex.practicum.commerce.shoppingstore.dal.dto.ProductDto;
import ru.yandex.practicum.commerce.shoppingstore.dal.entity.Product;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapper {

    public static Product toEntity(ProductDto productDto) {
        return Product.builder()
                .productId(Objects.nonNull(productDto.getProductId())
                        ? UUID.fromString(productDto.getProductId()) : null)
                .productName(productDto.getProductName())
                .description(productDto.getDescription())
                .imageSrc(productDto.getImageSrc())
                .quantityState(productDto.getQuantityState())
                .productState(productDto.getProductState())
                .productCategory(productDto.getProductCategory())
                .price(productDto.getPrice())
                .build();
    }

    public static ProductDto toDto(Product product) {
        return ProductDto.builder()
                .productId(product.getProductId().toString())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .quantityState(product.getQuantityState())
                .productState(product.getProductState())
                .productCategory(product.getProductCategory())
                .price(product.getPrice())
                .build();
    }
}
