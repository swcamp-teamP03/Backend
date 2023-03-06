package com.example.swcamp_p03.campaign.entity;

import lombok.AccessLevel;
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
    private String sendCheck;
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
}
