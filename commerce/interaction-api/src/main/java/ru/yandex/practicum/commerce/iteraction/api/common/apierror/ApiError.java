package ru.yandex.practicum.commerce.iteraction.api.common.apierror;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.commerce.iteraction.api.common.dto.StackTraceElementDto;
import ru.yandex.practicum.commerce.iteraction.api.common.dto.ThrowableDto;

import java.util.List;

@Data
@Builder
public class ApiError {

    private ThrowableDto cause;
    private List<StackTraceElementDto> stackTrace;
    private HttpStatus httpStatus;
    private String userMessage;
    private String message;
    private List<ThrowableDto> suppressed;
    private String localizedMessage;
}