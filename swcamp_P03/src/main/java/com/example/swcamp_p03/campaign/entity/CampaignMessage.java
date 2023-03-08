package com.example.swcamp_p03.campaign.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
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

    public void addSendRequestId(String sendRequestId){
        this.sendRequestId = sendRequestId;
    }
}
