package ru.yandex.practicum.commerce.iteraction.api.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ThrowableDto {
    private List<StackTraceElementDto> stackTrace;
    private String message;
    private String localizedMessage;
}
