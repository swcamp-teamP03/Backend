package com.example.swcamp_p03.customerGroup.entity.history;


import com.example.swcamp_p03.customerGroup.entity.ExcelFile;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExcelFileHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long excelFileHistoryId;
    private String excelFileOrgName;
    private String excelFileSavedName;
    private String excelFileSavedPath;
    private String excelFileSize;

    private LocalDateTime createdAt;

    @Builder(builderClassName = "historyRegister",builderMethodName = "historyRegister")
    public ExcelFileHistory(ExcelFile excelFile) {
        this.excelFileOrgName = excelFile != null ? excelFile.getExcelFileOrgName() : null;
        this.excelFileSavedName = excelFile != null ? excelFile.getExcelFileSavedName() : null;
        this.excelFileSavedPath = excelFile != null ? excelFile.getExcelFileSavedPath() : null;
        this.excelFileSize = excelFile != null ? excelFile.getExcelFileSize() : null;
        this.createdAt = excelFile != null ? excelFile.getExcelCreatedAt() : null;
    }
}
