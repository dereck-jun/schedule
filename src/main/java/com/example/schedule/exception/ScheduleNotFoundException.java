package com.example.schedule.exception;

import lombok.Getter;

@Getter
public class ScheduleNotFoundException extends RuntimeException {

    private ErrorCode errorCode;

    public ScheduleNotFoundException(String message) {
        super(message);
    }

    public ScheduleNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
