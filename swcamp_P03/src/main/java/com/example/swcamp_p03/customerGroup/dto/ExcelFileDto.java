package com.example.swcamp_p03.customerGroup.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ExcelFileDto {
    private String excelFileName;
    private String excelFileSize;
    private String excelUploadTime;
    private Long customerCnt;

    // bulk insert
    public ExcelFileDto(String excelFileName, String excelFileSize, LocalDateTime excelUploadTime, Long customerCnt) {
        this.excelFileName = excelFileName;
        this.excelFileSize = excelFileSize;
        this.excelUploadTime = excelUploadTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.customerCnt = customerCnt;
    }
}
