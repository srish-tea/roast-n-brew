package com.coffee.roastnbrew.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ApiResponse<T> {
    private boolean success;

    private T data;

    private String errorMessage;
    private Integer errorCode;

    public static <K> ApiResponse<K> successResponse(K data) {
        return ApiResponse.<K>builder().success(true)
                .data(data).build();
    }

    public static <K> ApiResponse<K> failureResponse(String errorMessage, int errorCode) {
        return ApiResponse.<K>builder()
                .success(false)
                .errorMessage(errorMessage)
                .errorCode(errorCode)
                .build();
    }
}
