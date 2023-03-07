package com.example.swcamp_p03.customerGroup.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class GroupCampaignDto {
    private String campaignName;
    private String createdAt;

    public GroupCampaignDto(String campaignName, LocalDateTime createdAt) {
        this.campaignName = campaignName;
        this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
