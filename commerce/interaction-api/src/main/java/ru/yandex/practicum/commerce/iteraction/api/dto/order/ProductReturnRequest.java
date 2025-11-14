package ru.yandex.practicum.commerce.iteraction.api.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReturnRequest {

    private UUID orderId;
    @NotNull
    private Map<UUID, Integer> products;
}
