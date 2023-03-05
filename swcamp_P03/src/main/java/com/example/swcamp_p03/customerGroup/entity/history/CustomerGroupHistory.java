package com.example.swcamp_p03.customerGroup.entity.history;

import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
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
public class CustomerGroupHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerGroupHistoryId;
    private String customerGroupName;
    private Boolean favorite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_GROUP_ID")
    private CustomerGroup customerGroup;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXCEL_FILE_HISTORY_ID")
    private ExcelFileHistory excelFileHistory;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "customerGroupHistory")
    private List<CustomerPropertyHistory> propertyHistoryList = new ArrayList<>();

    @Builder(builderClassName = "historyRegister", builderMethodName = "historyRegister")
    public CustomerGroupHistory(CustomerGroup customerGroup, ExcelFileHistory excelFileHistory) {
        this.customerGroupName = customerGroup.getCustomerGroupName();
        this.favorite = customerGroup.getFavorite();
        this.customerGroup = customerGroup;
        this.excelFileHistory = excelFileHistory;
        this.createdAt = customerGroup.getCreatedAt();
    }

}
