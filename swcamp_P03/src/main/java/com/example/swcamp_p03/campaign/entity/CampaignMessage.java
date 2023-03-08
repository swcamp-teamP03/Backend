package com.example.swcamp_p03.campaign.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CampaignMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long campaignMessageId;
    private String message;
    private String messageSection;
    private String sendRequestId;

    @ManyToOne
    @JoinColumn(name = "CAMPAIGN_ID")
    private Campaign campaign;

    @Builder(builderClassName = "testInsert",builderMethodName = "testInsert")
    public CampaignMessage(String message, String messageSection, Campaign campaign) {
        this.message = message;
        this.messageSection = messageSection;
        this.campaign = campaign;
    }
}
