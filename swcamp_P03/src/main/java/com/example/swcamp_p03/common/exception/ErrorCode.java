package com.example.swcamp_p03.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "Username is Duplicated", "사용 불가능한 아이디 입니다."),
    CANT_EDIT(HttpStatus.BAD_REQUEST, "CUSTOMER_GROUP_01","수정 할 권한이 없습니다."),
    NOT_EXCEL_FILE(HttpStatus.BAD_REQUEST, "CUSTOMER_GROUP_02","엑셀 파일이 아닙니다."),
    NOT_VALID_DOWNLOAD(HttpStatus.BAD_REQUEST,"CUSTOMER_GROUP_03","비밀번호가 일치 하지 않아 다운로드 할 수 없습니다.")
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
