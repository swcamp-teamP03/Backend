package com.example.swcamp_p03.campaign.repository;

import com.example.swcamp_p03.campaign.dto.response.TotalCampaignResponseDto;
import com.example.swcamp_p03.customerGroup.dto.SearchDto;
import com.example.swcamp_p03.user.entity.User;
import org.springframework.data.domain.Pageable;

public interface CampaignRepositoryCustom {
    TotalCampaignResponseDto findTotalCampaign(User user, Pageable pageable);

    TotalCampaignResponseDto findSearch(SearchDto searchDto);

}
