package com.example.swcamp_p03.customerGroup.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerPropertyId;
    private String propertyName;
    private String propertyValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_GROUP")
    private CustomerGroup customerGroup;

    @Builder
    public CustomerProperty(String propertyName, String propertyValue, CustomerGroup customerGroup) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
        this.customerGroup = customerGroup;
    }

    public void update(String propertyName, String propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }
}
