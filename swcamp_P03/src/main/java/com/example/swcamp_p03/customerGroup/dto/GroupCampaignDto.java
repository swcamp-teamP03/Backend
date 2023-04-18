package com.example.swcamp_p03.customerGroup.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class GroupCampaignDto {
    private Long campaignId;
    private String campaignName;
    private String createdAt;

    public GroupCampaignDto(Long campaignId, String campaignName, LocalDateTime createdAt) {
        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
