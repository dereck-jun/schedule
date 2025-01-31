package com.example.schedule.exception;

import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {

    private String field;
    private ErrorCode errorCode;

    public ClientException(ErrorCode errorCode, String message) {
        super(message);
    }

    public ClientException(String field, ErrorCode errorCode) {
        this.field = field;
        this.errorCode = errorCode;
    }
}
