package com.example.swcamp_p03.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "Username is Duplicated", "사용 불가능한 아이디 입니다."),
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "Bad Request", "잘못된 요청입니다."),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "Data Not Found", "존재하지 않는 데이터 입니다."),
    ;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    private final HttpStatus httpStatus;
    private String code;
    private String message;
}
