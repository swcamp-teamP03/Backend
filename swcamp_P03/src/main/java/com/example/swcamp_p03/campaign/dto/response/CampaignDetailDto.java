package com.example.swcamp_p03.campaign.dto.response;

import com.example.swcamp_p03.campaign.dto.CopyWriteABDto;
import com.example.swcamp_p03.campaign.dto.CustomerPropertyDto;
import com.example.swcamp_p03.campaign.dto.MessageADto;
import com.example.swcamp_p03.campaign.dto.MessageBDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class CampaignDetailDto {
    private Long campaignId;
    private String campaignName;
    private String campaignCreatedAt;
    private String sendingDateTime;
    private Long messageCount;
    private String messageType;
    private String groupName;
    private Long customerCount;
    private List<CustomerPropertyDto> customerProperties;
    private String excelOrgFileName;
    private String copyGroupName;
    private List<CopyWriteABDto> copyWriteAB;
    private MessageADto messageA;
    private MessageBDto messageB;
    private String comment;

    public CampaignDetailDto(Long campaignId,String campaignName, LocalDateTime campaignCreatedAt, LocalDateTime sendingDateTime, Long messageCount, String messageType, String groupName, String excelOrgFileName, String copyGroupName, String comment) {
        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.campaignCreatedAt = campaignCreatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.sendingDateTime = sendingDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
        this.messageCount = messageCount;
        this.messageType = messageType;
        this.groupName = groupName;
        this.customerCount = messageCount;
        this.excelOrgFileName = excelOrgFileName;
        this.copyGroupName = copyGroupName;
        this.comment = comment;
    }
}
