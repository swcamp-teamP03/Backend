package com.example.swcamp_p03.customerGroup.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ExcelFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long excelFileId;
    private String excelFileOrgName;
    private String excelFileSavedName;
    private String excelFileSavedPath;
    private String excelFileSize;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public ExcelFile(String excelFileOrgName, String excelFileSavedName, String excelFileSavedPath, String excelFileSize) {
        this.excelFileOrgName = excelFileOrgName;
        this.excelFileSavedName = excelFileSavedName;
        this.excelFileSavedPath = excelFileSavedPath;
        this.excelFileSize = excelFileSize;
    }

    public void update(String excelFileOrgName, String excelFileSavedName, String excelFileSavedPath, String excelFileSize) {
        this.excelFileOrgName = excelFileOrgName;
        this.excelFileSavedName = excelFileSavedName;
        this.excelFileSavedPath = excelFileSavedPath;
        this.excelFileSize = excelFileSize;
    }

}
