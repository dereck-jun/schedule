package com.example.schedule.controller.response;

import com.example.schedule.exception.ErrorDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private HttpStatus status;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private T data;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<ErrorDetail> errorDetails;
    private String message;
    private LocalDateTime timeStamp;

    public static <T> ApiResponse<T> success(HttpStatus status, T data, String message) {
        return new ApiResponse<>(true, status, data, null, message, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> failure(HttpStatus status, List<ErrorDetail> errorDetails, String message) {
        return new ApiResponse<>(false, status, null, errorDetails, message, LocalDateTime.now());
    }
}
