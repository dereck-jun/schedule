package com.example.schedule.controller;

import com.example.schedule.exception.ClientException;
import com.example.schedule.exception.ScheduleNotFoundException;
import com.example.schedule.controller.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

import static com.example.schedule.exception.ErrorCode.INVALID_ARGUMENT;
import static com.example.schedule.exception.ErrorCode.INVALID_DATE_FORMAT;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value = ScheduleNotFoundException.class)
    public ApiResponse<?> scheduleNotFoundExceptionHandler(ScheduleNotFoundException snfe) {
        return ApiResponse.failure(snfe.getErrorCode().getCode(), snfe.getErrorCode(), snfe.getErrorCode().getMessage());
    }

    @ExceptionHandler(value = ClientException.class)
    public ApiResponse<?> clientBadRequestExceptionHandler(ClientException ce) {
        return ApiResponse.failure(ce.getErrorCode().getCode(), ce.getErrorCode(), ce.getErrorCode().getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResponse<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException manve) {
        LocalDateTime now = LocalDateTime.now();
        log.error("MethodArgumentNotValidException: {}, timestamp: {}", manve.getParameter(), now);
        return ApiResponse.failure(INVALID_DATE_FORMAT.getCode(), INVALID_DATE_FORMAT, INVALID_DATE_FORMAT.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ApiResponse<?> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException matme) {
        LocalDateTime now = LocalDateTime.now();
        log.error("MethodArgumentTypeMismatchException: {}, timestamp: {}", matme.getParameter(), now);
        return ApiResponse.failure(INVALID_ARGUMENT.getCode(), INVALID_ARGUMENT, INVALID_ARGUMENT.getMessage());
    }
}
