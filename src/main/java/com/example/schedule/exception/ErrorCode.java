package com.example.schedule.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    BAD_REQUEST(4001, "잘못된 요청입니다."),
    INVALID_ARGUMENT(4002, "잘못된 인수입니다."),
    INVALID_DATE_FORMAT(4003, "잘못된 날짜 형식입니다."),
    INVALID_PASSWORD(4004, "비밀번호가 일치하지 않습니다."),
    INVALID_EMAIL(4005, "잘못된 이메일 형식입니다."),
    TYPE_MISMATCH(4006, "요청 필드의 타입이 올바르지 않습니다."),
    MISSING_PARAMETER(4007, "필수 요청 파라미터가 누락되었습니다."),
    INVALID_LENGTH(4008, "요청 필드의 길이가 너무 길거나 짧습니다."),
    INVALID_NUMBER_RANGE(4009, "0을 포함한 음수일 수 없습니다."),

    SCHEDULE_NOT_FOUND(4041, "요청에 해당하는 일정을 찾을 수 없습니다."),
    AUTHOR_NOT_FOUND(4042, "요청에 해당하는 작성자를 찾을 수 없습니다."),

    EMAIL_DUPLICATED(4091, "중복된 이메일입니다."),
    AUTHOR_NAME_DUPLICATED(4092, "중복된 작성자 이름입니다."),

    SERVER_ERROR(5001, "서버 내부 문제로 인해 요청을 받을 수 없습니다."),
    ;


    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
