package com.example.swcamp_p03.customerGroup.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExcelData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long excelDataId;
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "EXCEL_FILE_ID")
    private ExcelFile excelFile;
}
