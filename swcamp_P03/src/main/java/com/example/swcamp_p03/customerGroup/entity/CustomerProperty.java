package com.example.swcamp_p03.customerGroup.entity;

import com.example.swcamp_p03.customerGroup.dto.request.PropertyDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerPropertyId;
    private String propertyValue;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_GROUP_ID")
    private CustomerGroup customerGroup;

    @Builder(builderClassName = "register", builderMethodName = "register")
    public CustomerProperty(PropertyDto property, CustomerGroup customerGroup) {
        this.propertyValue = property != null ? property.getPropertyValue() : null;
        this.customerGroup = customerGroup;
        this.createdAt = LocalDateTime.now();
    }
}
