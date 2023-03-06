package com.example.swcamp_p03.customerGroup.dto;

import com.example.swcamp_p03.customerGroup.entity.CustomerProperty;
import lombok.Getter;

@Getter
public class GroupPropertyDto {
    private String propertyName;
    private String propertyValue;

    public GroupPropertyDto(CustomerProperty customerProperty) {
        this.propertyName = customerProperty.getPropertyName();
        this.propertyValue = customerProperty.getPropertyValue();
    }
}
