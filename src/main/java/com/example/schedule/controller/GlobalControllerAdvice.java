package com.example.schedule.controller;

import com.example.schedule.controller.response.ApiResponse;
import com.example.schedule.exception.ClientException;
import com.example.schedule.exception.ErrorCode;
import com.example.schedule.exception.ErrorDetail;
import com.example.schedule.exception.ScheduleNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.List;
import java.util.Objects;

import static com.example.schedule.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice(basePackages = "com.example.schedule")
public class GlobalControllerAdvice {

    @ExceptionHandler(value = ScheduleNotFoundException.class)
    public ApiResponse<ErrorDetail> scheduleNotFoundExceptionHandler(ScheduleNotFoundException snfe) {
        log.error("[ScheduleNotFoundException] : {}", "scheduleId에 해당하는 일정을 찾을 수 없음", snfe);
        ErrorDetail detail = new ErrorDetail("scheduleId", SCHEDULE_NOT_FOUND, "scheduleId에 해당하는 일정이 없습니다.");
        return ApiResponse.failure(
            NOT_FOUND,
            List.of(detail),
            "일정을 찾을 수 없습니다."
        );
    }

    @ExceptionHandler(value = ClientException.class)
    public ApiResponse<ErrorDetail> clientBadRequestExceptionHandler(ClientException ce) {
        log.error("[ClientException] : {}", "중복된 이메일로 인한 예외 발생", ce);
        ErrorCode errorCode = ce.getErrorCode();
        ErrorDetail detail = new ErrorDetail(ce.getField(), errorCode, errorCode.getMessage());
        if (errorCode == EMAIL_DUPLICATED || errorCode == AUTHOR_NAME_DUPLICATED) {
            return ApiResponse.failure(
                CONFLICT,
                List.of(detail),
                "중복된 필드로 인한 예외가 발생했습니다."
            );
        }
        return ApiResponse.failure(
            BAD_REQUEST,
            List.of(detail),
            "클라이언트 예외가 발생했습니다."
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResponse<ErrorDetail> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException manve) {
        log.error("[MethodArgumentNotValidException] : {}", manve.getMessage(), manve);

        List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();

        List<ErrorDetail> errorDetails = fieldErrors.stream()
            .map(fieldError -> {
                String fieldName = fieldError.getField();
                String code = fieldError.getCode();
                ErrorCode mappedErrorCode = getErrorCode(code);

                return new ErrorDetail(
                    fieldName,
                    mappedErrorCode,
                    mappedErrorCode.getMessage()
                );
            })
            .toList();
        return ApiResponse.failure(BAD_REQUEST, errorDetails, "필드 검증 오류가 발생했습니다.");
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ApiResponse<ErrorDetail> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException matme) {
        log.error("[MethodArgumentTypeMismatchException] : {}", matme.getMessage(), matme);

        String fieldName = matme.getName();
        String requiredType = (matme.getRequiredType() != null)
            ? matme.getRequiredType().getSimpleName()
            : "Unknown";

        String message = String.format(
            "필드 '%s'에 잘못된 타입이 전달되었습니다. (기대: %s, 전달값: %s)",
            fieldName,
            requiredType,
            matme.getValue()
        );

        ErrorDetail detail = new ErrorDetail(fieldName, TYPE_MISMATCH, message);

        return ApiResponse.failure(
            BAD_REQUEST,
            List.of(detail),
            "파라미터 타입 불일치 예외가 발생했습니다."
        );
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ApiResponse<ErrorDetail> handleMissingPathVariableException(MissingPathVariableException ex) {
        log.error("[MissingPathVariableException] : {}", ex.getMessage(), ex);

        String variableName = ex.getVariableName();
        ErrorDetail detail = new ErrorDetail(
            variableName,
            INVALID_ARGUMENT,
            "필수 PathVariable이 누락되었습니다."
        );

        return ApiResponse.failure(
            BAD_REQUEST,
            List.of(detail),
            "PathVariable 누락 예외가 발생했습니다."
        );
    }

    private ErrorCode getErrorCode(String code) {
        switch (Objects.requireNonNull(code)) {
            case "Max", "Min", "Size" -> {
                return INVALID_LENGTH;
            }
            case "NotBlank" -> {
                return MISSING_PARAMETER;
            }
            case "Pattern" -> {
                return INVALID_EMAIL;
            }
            case "typeMismatch" -> {
                return TYPE_MISMATCH;
            }
            case "Positive" -> {
                return INVALID_NUMBER_RANGE;
            }
            default -> {
                return ErrorCode.BAD_REQUEST;
            }
        }
    }
}
