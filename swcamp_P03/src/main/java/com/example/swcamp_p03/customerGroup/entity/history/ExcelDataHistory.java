package com.example.swcamp_p03.customerGroup.entity.history;

import com.example.swcamp_p03.customerGroup.entity.ExcelData;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExcelDataHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long excelDataHistoryId;
    private String username;
    private String phoneNumber;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXCEL_FILE_HiSTORY_ID")
    private ExcelFileHistory excelFileHistory;

    @Builder(builderClassName = "historyRegister", builderMethodName = "historyRegister")
    public ExcelDataHistory(ExcelData excelData, ExcelFileHistory excelFileHistory) {
        this.username = excelData != null ? excelData.getUsername() : null;
        this.phoneNumber = excelData != null ? excelData.getPhoneNumber() : null;
        this.excelFileHistory = excelFileHistory;
    }
}
