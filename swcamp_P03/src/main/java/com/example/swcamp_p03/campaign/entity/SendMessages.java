package com.example.swcamp_p03.campaign.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SendMessages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sendMessagesId;
    private String name;
    private String phoneNumber;
    private Boolean sendCheck;
    private String sendState;
    private String errorMessage;
    @Column(unique = true)
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

    public void updateData(String sendState,String errorMessage){
        this.sendState = sendState;
        this.errorMessage = errorMessage;
    }
}
