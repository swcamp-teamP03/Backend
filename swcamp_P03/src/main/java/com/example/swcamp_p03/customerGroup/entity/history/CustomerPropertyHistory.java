package com.example.swcamp_p03.customerGroup.entity.history;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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


}
