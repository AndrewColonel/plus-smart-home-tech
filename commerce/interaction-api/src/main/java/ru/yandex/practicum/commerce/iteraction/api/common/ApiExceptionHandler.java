package ru.yandex.practicum.commerce.iteraction.api.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.iteraction.api.exception.BaseException;

import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler
    public <T extends BaseException> ResponseEntity<ApiError> handleNotFound(T ex) {

        ApiError error = ApiError.builder()
                .cause(getCause(ex))
                .stackTrace(getStackTreces(ex))
                .httpStatus(ex.getHttpStatus())
                .userMessage(ex.getUserMessage())
                .message(ex.getMessage())
                .suppressed(getSupressed(ex))
                .localizedMessage(ex.getLocalizedMessage())
                .build();

        return new ResponseEntity<>(error, ex.getHttpStatus());
    }

    private <T extends BaseException> CauseDto getCause(T ex) {
        return CauseDto.builder()
                .stackTrace(getStackTreces(ex))
                .message(ex.getMessage())
                .localizedMessage(ex.getLocalizedMessage())
                .build();
    }

    private <T extends BaseException> SuppressedDto getSupressed(T ex) {
        return SuppressedDto.builder()
                .stackTrace(getStackTreces(ex))
                .message(ex.getMessage())
                .localizedMessage(ex.getLocalizedMessage())
                .build();
    }

    private <T extends BaseException> List<StackTraceElementDto> getStackTreces(T ex) {
        return Arrays.stream(ex.getStackTrace())
                .map(StackTraceElementDto::toDto)
                .toList();
    }
}