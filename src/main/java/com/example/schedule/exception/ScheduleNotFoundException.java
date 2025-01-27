package com.example.schedule.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ScheduleNotFoundException extends RuntimeException {

    private ErrorCode errorCode;

    public ScheduleNotFoundException(String message) {
        super(message);
    }

    @Builder
    public ScheduleNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
