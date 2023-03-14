package com.example.swcamp_p03.customerGroup.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExcelFile {
    private String excelFileOrgName;
    private String excelFileSavedName;
    private String excelFileSavedPath;
    private String excelFileSize;

    @Column(nullable = false)
    private LocalDateTime excelCreatedAt;

    @ElementCollection
    @CollectionTable(name = "EXCEL_DATA",
            joinColumns = @JoinColumn(name = "EXCEL_DATA_ID"))
    private List<ExcelData> excelDatas = new ArrayList<>();

    @Builder(builderClassName = "register", builderMethodName = "register")
    public ExcelFile(String excelFileOrgName, String excelFileSavedName, String excelFileSavedPath, String excelFileSize) {
        this.excelFileOrgName = excelFileOrgName;
        this.excelFileSavedName = excelFileSavedName;
        this.excelFileSavedPath = excelFileSavedPath;
        this.excelFileSize = excelFileSize;
        this.excelCreatedAt = LocalDateTime.now();
    }

    public void update(String excelFileOrgName, String excelFileSavedName, String excelFileSavedPath, String excelFileSize) {
        this.excelFileOrgName = excelFileOrgName;
        this.excelFileSavedName = excelFileSavedName;
        this.excelFileSavedPath = excelFileSavedPath;
        this.excelFileSize = excelFileSize;
    }

}
