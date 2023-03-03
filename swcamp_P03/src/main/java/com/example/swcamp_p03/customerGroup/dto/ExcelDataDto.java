package com.example.swcamp_p03.customerGroup.dto;

import com.example.swcamp_p03.customerGroup.entity.ExcelFile;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ExcelDataDto {
    private String username;
    private String phoneNumber;
    private ExcelFile excelFile;
    @Builder
    public ExcelDataDto(String username, String phoneNumber, ExcelFile excelFile) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.excelFile = excelFile;
    }
}
