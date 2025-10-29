package ru.yandex.practicum.commerce.iteraction.api.common;

import org.springframework.http.HttpStatus;
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
                .cause(getThrowable(ex.getCause()))
                .stackTrace(getStackTreces(ex))
                .httpStatus(ex.getHttpStatus())
                .userMessage(ex.getUserMessage())
                .message(ex.getMessage())
                .suppressed(Arrays.stream(ex.getSuppressed())
                        .map(this::getThrowable)
                        .toList())
                .localizedMessage(ex.getLocalizedMessage())
                .build();

        return new ResponseEntity<>(error, ex.getHttpStatus());
    }

    private <T extends Throwable> ThrowableDto getThrowable(T ex) {
        return ThrowableDto.builder()
                .stackTrace(Arrays.stream(ex.getStackTrace())
                        .map(StackTraceElementDto::toDto)
                        .toList())
                .message(ex.getMessage())
                .localizedMessage(ex.getLocalizedMessage())
                .build();
    }

    private <T extends BaseException> List<StackTraceElementDto> getStackTreces(T ex) {
        return Arrays.stream(ex.getStackTrace())
                .map(StackTraceElementDto::toDto)
                .toList();
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleRunTime(RuntimeException ex) {

        ApiError error = ApiError.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(ex.getMessage())
                .suppressed(Arrays.stream(ex.getSuppressed())
                        .map(this::getThrowable)
                        .toList())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}