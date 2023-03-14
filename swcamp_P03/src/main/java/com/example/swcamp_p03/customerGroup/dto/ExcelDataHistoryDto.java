package com.example.swcamp_p03.customerGroup.dto;

import com.example.swcamp_p03.customerGroup.entity.ExcelData;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExcelDataHistoryDto {
    private String username;
    private String phoneNumber;
    private Long excelFileHistoryId;
    private LocalDateTime createdAt;

    @Builder(builderClassName = "historyRegister", builderMethodName = "historyRegister")
    public ExcelDataHistoryDto(ExcelData excelData, Long excelFileHistoryId) {
        this.username = excelData != null ? excelData.getUsername() : null;
        this.phoneNumber = excelData != null ? excelData.getPhoneNumber() : null;
        this.excelFileHistoryId = excelFileHistoryId;
    }
}
