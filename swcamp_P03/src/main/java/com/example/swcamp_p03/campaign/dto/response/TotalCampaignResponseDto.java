package com.example.swcamp_p03.campaign.dto.response;

import com.example.swcamp_p03.campaign.dto.CampaignDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Getter
@Setter
public class TotalCampaignResponseDto {
    private int totalCampaignPage;
    private List<CampaignDto> campaignList;

    public TotalCampaignResponseDto(PageImpl<CampaignDto> campaignListDtos) {
        this.totalCampaignPage = campaignListDtos.getTotalPages();
        this.campaignList = campaignListDtos.getContent();
    }
}
