package com.example.swcamp_p03.customerGroup.entity;

import lombok.AccessLevel;
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

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_GROUP")
    private CustomerGroup customerGroup;
}
