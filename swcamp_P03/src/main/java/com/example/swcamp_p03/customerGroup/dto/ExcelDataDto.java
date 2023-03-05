package com.example.swcamp_p03.customerGroup.dto;

import com.example.swcamp_p03.customerGroup.entity.ExcelFile;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

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
