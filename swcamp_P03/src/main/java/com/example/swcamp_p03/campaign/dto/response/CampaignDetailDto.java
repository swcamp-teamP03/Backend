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
    public String campaignCreatedAt;
    public String sendingDateTime;
    public Long messageCount;
    public String messageType;
    public String groupName;
    public Long customerCount;
    public List<CustomerPropertyDto> customerProperties;
    public String excelOrgFileName;
    public String copyGroupName;
    public List<CopyWriteABDto> copyWriteAB;
    public MessageADto messageA;
    public MessageBDto messageB;
    public String comment;

    public CampaignDetailDto(LocalDateTime campaignCreatedAt, LocalDateTime sendingDateTime, Long messageCount, String messageType, String groupName, String excelOrgFileName, String copyGroupName, String comment) {
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
