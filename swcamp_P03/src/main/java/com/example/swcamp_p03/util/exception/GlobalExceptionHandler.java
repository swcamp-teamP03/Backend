package com.example.swcamp_p03.util.exception;

import com.example.swcamp_p03.util.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    protected ResponseEntity<?> handleGlobalException(GlobalException e) {

        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ResponseDto.fail(
                        e.getErrorCode().getCode(),
                        e.getErrorCode().getMessage())
                );
    }
}
