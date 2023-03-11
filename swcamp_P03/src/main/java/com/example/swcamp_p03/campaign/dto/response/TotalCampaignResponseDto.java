package com.example.swcamp_p03.campaign.dto.response;

import com.example.swcamp_p03.campaign.dto.CampaignDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Getter
@Setter
public class TotalCampaignResponseDto {
    private Long totalCampaignCount;
    private int totalCampaignPage;
    private List<CampaignDto> campaignList;

    public TotalCampaignResponseDto(Long totalCampaignCount, PageImpl<CampaignDto> campaignListDtos) {
        this.totalCampaignCount = totalCampaignCount;
        this.totalCampaignPage = campaignListDtos.getTotalPages();
        this.campaignList = campaignListDtos.getContent();
    }
}
