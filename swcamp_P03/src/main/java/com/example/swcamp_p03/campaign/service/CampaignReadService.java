package com.example.swcamp_p03.campaign.service;

import com.example.swcamp_p03.campaign.dto.CampaignDto;
import com.example.swcamp_p03.campaign.dto.response.TotalCampaignResponseDto;
import com.example.swcamp_p03.campaign.repository.CampaignRepository;
import com.example.swcamp_p03.common.dto.ResponseDto;
import com.example.swcamp_p03.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignReadService {
    private final CampaignRepository campaignRepository;

    public ResponseDto<TotalCampaignResponseDto> getTotalCampaign(User user, Pageable pageable) {
        List<CampaignDto> campaign = campaignRepository.findCampaign(user, pageable);
        Long countCampaign = campaignRepository.countCampaign(user);
        PageImpl<CampaignDto> campaignListDtos = new PageImpl<>(campaign, pageable, countCampaign);
        return ResponseDto.success(new TotalCampaignResponseDto(campaignListDtos));
    }
}
