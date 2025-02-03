package com.example.schedule.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {

    private ErrorCode errorCode;

    public ClientException(String message) {
        super(message);
    }

    @Builder
    public ClientException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
