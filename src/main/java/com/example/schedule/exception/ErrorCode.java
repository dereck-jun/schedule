package com.example.schedule.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_ARGUMENT(400, "잘못된 인수입니다."),
    INVALID_DATE_FORMAT(400, "잘못된 날짜 형식입니다."),
    INVALID_PASSWORD(400, "비밀번호가 일치하지 않습니다."),
    SCHEDULE_NOT_FOUND(404, "요청에 해당하는 일정을 찾을 수 없습니다."),
    AUTHOR_NOT_FOUND(404, "요청에 해당하는 작성자를 찾을 수 없습니다."),

    SERVER_ERROR(500, "서버 내부 문제로 인해 요청을 받을 수 없습니다."),
    ;


    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
