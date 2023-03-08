package com.example.swcamp_p03.campaign.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendMessages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sendMessagesId;
    private String name;
    private String phoneNumber;
    private Boolean sendCheck;
    private String sendState;
    private String errorMessage;
    private String uniqueUrl;
    private LocalDateTime sendDateTime;
    private LocalDateTime visitedTime;
    private LocalDate visitedDate;

    @ManyToOne
    @JoinColumn(name = "CAMPAIGN_MESSAGE_ID")
    private CampaignMessage campaignMessage;

    @ManyToOne
    @JoinColumn(name = "CAMPAIGN_ID")
    private Campaign campaign;

    @Builder(builderClassName = "testInsert",builderMethodName = "testInsert")
    public SendMessages(String name, Boolean sendCheck, LocalDateTime sendDateTime, CampaignMessage campaignMessage, Campaign campaign) {
        this.name = name;
        this.sendCheck = sendCheck;
        this.sendDateTime = sendDateTime;
        this.campaignMessage = campaignMessage;
        this.campaign = campaign;
    }
}
