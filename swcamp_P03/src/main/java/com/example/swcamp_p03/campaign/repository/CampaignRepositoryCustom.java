package com.example.swcamp_p03.campaign.repository;

import com.example.swcamp_p03.campaign.dto.CopyWriteABDto;
import com.example.swcamp_p03.campaign.dto.CustomerPropertyDto;
import com.example.swcamp_p03.campaign.dto.MessageADto;
import com.example.swcamp_p03.campaign.dto.MessageBDto;
import com.example.swcamp_p03.campaign.dto.response.CampaignDetailDto;
import com.example.swcamp_p03.campaign.dto.response.TotalCampaignResponseDto;
import com.example.swcamp_p03.common.dto.SearchDto;
import com.example.swcamp_p03.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CampaignRepositoryCustom {
    TotalCampaignResponseDto findTotalCampaign(User user, Pageable pageable);

    TotalCampaignResponseDto findSearch(SearchDto searchDto);

    CampaignDetailDto findByCampaign(Long campaignId);

    List<CustomerPropertyDto> findByCustomerGroupToProperties(Long campaignId);

    List<CopyWriteABDto> findByCopyGroupToCopyWrites(Long campaignId);

    MessageADto findByMessageACount(Long campaignId);

    MessageBDto findByMessageBCount(Long campaignId);
}
