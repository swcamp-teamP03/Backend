package com.example.swcamp_p03.customerGroup.dto;

import com.example.swcamp_p03.customerGroup.entity.CustomerProperty;
import lombok.Getter;

@Getter
public class GroupPropertyDto {
    private String propertyValue;

    public GroupPropertyDto(CustomerProperty customerProperty) {
        this.propertyValue = customerProperty.getPropertyValue();
    }
}
