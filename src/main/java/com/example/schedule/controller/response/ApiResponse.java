package com.example.schedule.controller.response;

import com.example.schedule.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private int code;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private T data;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private ErrorCode errorCode;
    private String message;
    private LocalDateTime timeStamp;

    public static <T> ApiResponse<T> success(int code, T data, String message) {
        return new ApiResponse<>(true, code, data, null, message, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> failure(int code, ErrorCode errorCode, String message) {
        return new ApiResponse<>(false, code, null, errorCode, message, LocalDateTime.now());
    }
}
