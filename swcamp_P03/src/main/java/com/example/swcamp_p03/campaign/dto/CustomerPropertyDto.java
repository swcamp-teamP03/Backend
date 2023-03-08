package com.example.swcamp_p03.campaign.dto;

import lombok.Getter;

@Getter
public class CustomerPropertyDto {
    public String propertyValue;

    public CustomerPropertyDto(String propertyValue) {
        this.propertyValue = propertyValue;
    }
}
