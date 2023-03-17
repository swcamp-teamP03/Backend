package com.example.swcamp_p03.campaign.dto;

import lombok.Getter;

@Getter
public class CampaignRateDto {
    private Double clickRate;

    public CampaignRateDto(Long clickCount, Long totalClickCount) {
        double clickThroughRate = clickCount / (double) totalClickCount;
        this.clickRate = Math.round(clickThroughRate * 10000) / 100.0;

    }
}
