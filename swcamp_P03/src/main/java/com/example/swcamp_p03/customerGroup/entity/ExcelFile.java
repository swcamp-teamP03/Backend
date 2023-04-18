package com.example.swcamp_p03.customerGroup.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExcelFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long excelFileId;
    private String excelFileOrgName;
    private String excelFileSavedName;
    private String excelFileSavedPath;
    private String excelFileSize;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder(builderClassName = "register", builderMethodName = "register")
    public ExcelFile(String excelFileOrgName, String excelFileSavedName, String excelFileSavedPath, String excelFileSize) {
        this.excelFileOrgName = excelFileOrgName;
        this.excelFileSavedName = excelFileSavedName;
        this.excelFileSavedPath = excelFileSavedPath;
        this.excelFileSize = excelFileSize;
        this.createdAt = LocalDateTime.now();
    }

    public void update(String excelFileOrgName, String excelFileSavedName, String excelFileSavedPath, String excelFileSize) {
        this.excelFileOrgName = excelFileOrgName;
        this.excelFileSavedName = excelFileSavedName;
        this.excelFileSavedPath = excelFileSavedPath;
        this.excelFileSize = excelFileSize;
    }

}
