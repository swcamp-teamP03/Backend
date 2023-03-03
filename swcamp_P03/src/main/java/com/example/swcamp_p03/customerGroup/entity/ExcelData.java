package com.example.swcamp_p03.customerGroup.entity;

import com.example.swcamp_p03.customerGroup.dto.ExcelDataDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExcelData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long excelDataId;
    private String username;
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXCEL_FILE_ID")
    private ExcelFile excelFile;
//    private Long excelFileId;

    public ExcelData(ExcelDataDto excelDataDto) {
        this.username = excelDataDto.getUsername();
        this.phoneNumber = excelDataDto.getPhoneNumber();
        this.excelFile = excelDataDto.getExcelFile();
    }
    @Builder
    public ExcelData(String username, String phoneNumber, ExcelFile excelFile) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.excelFile = excelFile;
    }
}
