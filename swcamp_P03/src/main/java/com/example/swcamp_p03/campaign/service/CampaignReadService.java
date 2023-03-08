package com.example.swcamp_p03.campaign.service;

import com.example.swcamp_p03.campaign.dto.response.TotalCampaignResponseDto;
import com.example.swcamp_p03.campaign.repository.CampaignRepository;
import com.example.swcamp_p03.common.dto.ResponseDto;
import com.example.swcamp_p03.customerGroup.dto.SearchDto;
import com.example.swcamp_p03.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CampaignReadService {
    private final CampaignRepository campaignRepository;

    public ResponseDto<TotalCampaignResponseDto> getTotalCampaign(User user, Pageable pageable) {
        TotalCampaignResponseDto totalCampaign = campaignRepository.findTotalCampaign(user, pageable);
        return ResponseDto.success(totalCampaign);
    }

    public ResponseDto<TotalCampaignResponseDto> getSearch(SearchDto searchDto) {
        TotalCampaignResponseDto search = campaignRepository.findSearch(searchDto);
        return ResponseDto.success(search);
    }
}
