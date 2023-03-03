package com.example.swcamp_p03.customerGroup.dto;

import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class ExcelFileDto {
    private String excelFileName;
    private String excelFileSize;
    private LocalDateTime excelUploadTime;
    private Long customerCnt;

    public ExcelFileDto(String excelFileName, String excelFileSize, LocalDateTime excelUploadTime, Long customerCnt) {
        this.excelFileName = excelFileName;
        this.excelFileSize = excelFileSize;
        this.excelUploadTime = excelUploadTime;
        this.customerCnt = customerCnt;
    }
}
