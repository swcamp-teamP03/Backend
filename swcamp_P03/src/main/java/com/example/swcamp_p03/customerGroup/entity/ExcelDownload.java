package com.example.swcamp_p03.customerGroup.entity;

import com.example.swcamp_p03.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExcelDownload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long excelDownloadId;
    private String downloadReason;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public ExcelDownload(String downloadReason, User user) {
        this.downloadReason = downloadReason;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }
}
