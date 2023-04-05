package com.example.swcamp_p03.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "Username is Duplicated", "사용 불가능한 아이디 입니다."),
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "Bad Request", "잘못된 요청입니다."),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "Data Not Found", "존재하지 않는 데이터 입니다."),
    CANT_EDIT(HttpStatus.BAD_REQUEST, "CUSTOMER_GROUP_01","수정 할 권한이 없습니다."),
    NOT_EXCEL_FILE(HttpStatus.BAD_REQUEST, "CUSTOMER_GROUP_02","엑셀 파일이 아닙니다."),
    NOT_VALID_DOWNLOAD(HttpStatus.BAD_REQUEST,"CUSTOMER_GROUP_03","비밀번호가 일치 하지 않아 다운로드 할 수 없습니다."),
    NOT_VALID_EXCEL_USERNAME(HttpStatus.BAD_REQUEST,"CUSTOMER_GROUP_04","이름이 유효하지 않습니다."),
    NOT_VALID_EXCEL_PHONE_NUMBER(HttpStatus.BAD_REQUEST,"CUSTOMER_GROUP_05","전화번호가 유효하지 않습니다."),
    NOT_UPLOAD_FILE(HttpStatus.BAD_REQUEST,"CUSTOMER_GROUP_06","파일을 업로드 해주세요."),
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "USER_01", "존재하는 이메일 입니다."),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "USER_02", "토큰 유효시간이 지났습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "USER_03", "유효하지 않는 토큰입니다."),
    INVALID_USER(HttpStatus.BAD_REQUEST, "USER_04","아이디 또는 비밀번호가 유효하지 않습니다."),
    UPDATE_SEND_MESSAGE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "updateSendMessages fail","updateSendMessages 업데이트가 실패 했습니다."),
    RETRY_EMAIL_CERTIFICATION(HttpStatus.BAD_REQUEST,"EMAIL_01","인증을 재시도 해주세요."),
    EMAIL_CERTIFICATION_EXPIRED(HttpStatus.BAD_REQUEST,"EMAIL_02","인증 시간이 초과되었습니다. 재시도 해주세요.")
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
