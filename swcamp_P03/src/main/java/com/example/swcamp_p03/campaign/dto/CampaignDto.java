package com.example.swcamp_p03.campaign.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class CampaignDto {
    private Long campaignId;
    private boolean favorite;
    private String messageType;
    private String campaignName;
    private String createdAt;
    private String sendingDate;
    private String sendState;
    private Double clickRate;

    public CampaignDto(Long campaignId,boolean favorite, String messageType, String campaignName, LocalDateTime createdAt, LocalDateTime sendingDate, Long clickCount, Long totalClickCount) {
        this.campaignId = campaignId;
        this.favorite = favorite;
        this.messageType = messageType;
        this.campaignName = campaignName;
        this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.sendingDate = sendingDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
        this.sendState = sendingDate.isBefore(LocalDateTime.now()) ? "발송완료" : "발송대기";
        double clickThroughRate = clickCount / (double) totalClickCount;
        double clickRate = Math.round(clickThroughRate * 10000) / 100.0;
        this.clickRate = clickRate;
    }
}
