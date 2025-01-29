package com.example.schedule.controller;

import com.example.schedule.controller.response.ApiResponse;
import com.example.schedule.exception.ClientException;
import com.example.schedule.exception.ScheduleNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.example.schedule.exception.ErrorCode.*;

@Slf4j
@RestControllerAdvice(basePackages = "com.example.schedule")
public class GlobalControllerAdvice {

    @ExceptionHandler(value = ScheduleNotFoundException.class)
    public ApiResponse<?> scheduleNotFoundExceptionHandler(ScheduleNotFoundException snfe) {
        printErrorLog(snfe);
        return ApiResponse.failure(snfe.getErrorCode().getCode(), snfe.getErrorCode(), snfe.getErrorCode().getMessage());
    }

    @ExceptionHandler(value = ClientException.class)
    public ApiResponse<?> clientBadRequestExceptionHandler(ClientException ce) {
        printErrorLog(ce);
        return ApiResponse.failure(ce.getErrorCode().getCode(), ce.getErrorCode(), ce.getErrorCode().getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResponse<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException manve) {
        printErrorLog(manve);
        List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();

        for (FieldError fieldError : fieldErrors) {
            String code = fieldError.getCode();

            switch (Objects.requireNonNull(code)) {
                case "Max", "Min", "Size" -> {
                    return ApiResponse.failure(INVALID_LENGTH.getCode(), INVALID_LENGTH, INVALID_LENGTH.getMessage());
                }
                case "NotBlank" -> {
                    return ApiResponse.failure(MISSING_PARAMETER.getCode(), MISSING_PARAMETER, MISSING_PARAMETER.getMessage());
                }
                case "Pattern" -> {
                    return ApiResponse.failure(INVALID_EMAIL.getCode(), INVALID_EMAIL, INVALID_EMAIL.getMessage());
                }
                case "typeMismatch" -> {
                    return ApiResponse.failure(TYPE_MISMATCH.getCode(), TYPE_MISMATCH, TYPE_MISMATCH.getMessage());
                }
                case "Positive" -> {
                    return ApiResponse.failure(INVALID_NUMBER_RANGE.getCode(), INVALID_NUMBER_RANGE, INVALID_NUMBER_RANGE.getMessage());
                }
            }
        }
        return ApiResponse.failure(INVALID_ARGUMENT.getCode(), INVALID_ARGUMENT, INVALID_ARGUMENT.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ApiResponse<?> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException matme) {
        printErrorLog(matme);
        String errorCode = matme.getErrorCode();

        switch (Objects.requireNonNull(errorCode)) {
            case "Max", "Min", "Size" -> {
                return ApiResponse.failure(INVALID_LENGTH.getCode(), INVALID_LENGTH, INVALID_LENGTH.getMessage());
            }
            case "NotBlank" -> {
                return ApiResponse.failure(MISSING_PARAMETER.getCode(), MISSING_PARAMETER, MISSING_PARAMETER.getMessage());
            }
            case "Pattern" -> {
                return ApiResponse.failure(INVALID_EMAIL.getCode(), INVALID_EMAIL, INVALID_EMAIL.getMessage());
            }
            case "typeMismatch" -> {
                return ApiResponse.failure(TYPE_MISMATCH.getCode(), TYPE_MISMATCH, TYPE_MISMATCH.getMessage());
            }
            case "Positive" -> {
                return ApiResponse.failure(INVALID_NUMBER_RANGE.getCode(), INVALID_NUMBER_RANGE, INVALID_NUMBER_RANGE.getMessage());
            }
        }
        return ApiResponse.failure(INVALID_ARGUMENT.getCode(), INVALID_ARGUMENT, INVALID_ARGUMENT.getMessage());
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ApiResponse<?> handleMissingPathVariableException(MissingPathVariableException ex) {
        // 예외 정보 로깅
        String variableName = ex.getVariableName();
        log.error("Missing path variable: {}", variableName);

        // 커스텀 응답 생성
        return ApiResponse.failure(INVALID_ARGUMENT.getCode(), INVALID_ARGUMENT, INVALID_ARGUMENT.getMessage());
    }

    private void printErrorLog(Throwable throwable) {
        LocalDateTime now = LocalDateTime.now();
        log.error("{}: {}, timestamp: {}", throwable.getClass().getName(), throwable.getLocalizedMessage(), now);
    }
}
