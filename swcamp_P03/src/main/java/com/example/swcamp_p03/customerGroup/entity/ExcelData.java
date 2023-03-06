package com.example.swcamp_p03.customerGroup.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXCEL_FILE_ID")
    private ExcelFile excelFile;

    @Builder(builderClassName = "testInsert", builderMethodName = "testInsert")
    public ExcelData(String username, String phoneNumber, ExcelFile excelFile) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.excelFile = excelFile;
    }
}
