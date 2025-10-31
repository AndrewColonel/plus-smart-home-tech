package ru.yandex.practicum.commerce.iteraction.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StackTraceElementDto {
    private String classLoaderName;
    private String moduleName;
    private String moduleVersion;
    private String methodName;
    private String fileName;
    private Integer lineNumber;
    private String className;
//    @JsonProperty("nativeMethod")
    private Boolean nativeMethod;

    public static StackTraceElementDto toDto(StackTraceElement stackTrace) {
        return StackTraceElementDto.builder()
                .classLoaderName(stackTrace.getClassLoaderName())
                .moduleName(stackTrace.getModuleName())
                .moduleVersion(stackTrace.getModuleVersion())
                .methodName(stackTrace.getMethodName())
                .fileName(stackTrace.getFileName())
                .lineNumber(stackTrace.getLineNumber())
                .className(stackTrace.getClassName())
                .nativeMethod(stackTrace.isNativeMethod())
                .build();
    }

}
