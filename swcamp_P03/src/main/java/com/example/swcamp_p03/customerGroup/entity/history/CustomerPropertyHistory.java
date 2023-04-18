package com.example.swcamp_p03.customerGroup.entity.history;

import com.example.swcamp_p03.customerGroup.entity.CustomerProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerPropertyHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerPropertyHistoryId;
    private String propertyValue;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_GROUP_HISTORY_ID")
    private CustomerGroupHistory customerGroupHistory;

    @Builder(builderClassName = "historyRegister",builderMethodName = "historyRegister")
    public CustomerPropertyHistory(CustomerProperty customerProperty, CustomerGroupHistory customerGroupHistory) {
        this.propertyValue = customerProperty.getPropertyValue();
        this.customerGroupHistory = customerGroupHistory;
        this.createdAt = customerProperty.getCreatedAt();
    }

}
