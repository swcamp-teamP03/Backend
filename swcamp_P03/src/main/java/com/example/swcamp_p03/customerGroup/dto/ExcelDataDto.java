package com.example.swcamp_p03.customerGroup.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
public class ExcelDataDto {
    private String username;
    private String phoneNumber;
    private Long excelFileId;
    private LocalDateTime createdAt;

    @Builder(builderClassName = "excelDataRegister", builderMethodName = "excelDataRegister")
    public ExcelDataDto(String username, String phoneNumber, Long excelFileId) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.excelFileId = excelFileId;
        this.createdAt = LocalDateTime.now();
    }
}
