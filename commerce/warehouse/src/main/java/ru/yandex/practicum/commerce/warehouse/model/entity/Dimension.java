package ru.yandex.practicum.commerce.warehouse.model.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dimension {
    private Double width;
    private Double height;
    private Double depth;
}
