package com.example.swcamp_p03.campaign.dto.request;

import lombok.Data;

@Data
public class CampaignRequestDto {
    private String campaignName;
    private Long customerGroupId;
    private Long copyGroupId;
    private String messageType;
    private String sendType;
    private String sendURL;
    private String sendingDate;
    private String messageA;
    private String messageB;
}
