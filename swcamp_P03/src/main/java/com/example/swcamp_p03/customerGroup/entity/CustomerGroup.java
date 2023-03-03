package com.example.swcamp_p03.customerGroup.entity;

import com.example.swcamp_p03.user.entity.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class CustomerGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerGroupId;
    private String customerGroupName;
    private Boolean favorite;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXCEL_FILE_ID")
    private ExcelFile excelFile;

    @OneToMany(mappedBy = "customerGroup")
    private List<CustomerProperty> propertyList = new ArrayList<>();

    @Builder
    public CustomerGroup(String customerGroupName, Boolean favorite, LocalDateTime createdAt, User user, ExcelFile excelFile) {
        this.customerGroupName = customerGroupName;
        this.favorite = favorite;
        this.createdAt = createdAt;
        this.user = user;
        this.excelFile = excelFile;
    }

    public void excelFileUpdate(String customerGroupName, ExcelFile excelFile) {
        this.customerGroupName = customerGroupName;
        this.excelFile = excelFile;
    }

    public void update(String customerGroupName) {
        this.customerGroupName = customerGroupName;
    }

}
