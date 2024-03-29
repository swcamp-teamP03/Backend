package com.example.swcamp_p03.campaign.service;

import com.example.swcamp_p03.campaign.dto.CopyWriteABDto;
import com.example.swcamp_p03.campaign.dto.CustomerPropertyDto;
import com.example.swcamp_p03.campaign.dto.MessageADto;
import com.example.swcamp_p03.campaign.dto.MessageBDto;
import com.example.swcamp_p03.campaign.dto.response.CampaignDetailDto;
import com.example.swcamp_p03.campaign.dto.response.TotalCampaignResponseDto;
import com.example.swcamp_p03.campaign.entity.Campaign;
import com.example.swcamp_p03.campaign.repository.CampaignRepository;
import com.example.swcamp_p03.common.dto.ResponseDto;
import com.example.swcamp_p03.common.dto.SearchDto;
import com.example.swcamp_p03.common.exception.ErrorCode;
import com.example.swcamp_p03.common.exception.GlobalException;
import com.example.swcamp_p03.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignReadService {
    private final CampaignRepository campaignRepository;
    private final CampaignService campaignService;

    public ResponseDto<TotalCampaignResponseDto> getTotalCampaign(User user, Pageable pageable) {
        campaignRepository.updateClickRate(user);
        TotalCampaignResponseDto totalCampaign = campaignRepository.findTotalCampaign(user, pageable);
        return ResponseDto.success(totalCampaign);
    }

    public ResponseDto<TotalCampaignResponseDto> getSearch(SearchDto searchDto) {
        TotalCampaignResponseDto search = campaignRepository.findSearch(searchDto);
        return ResponseDto.success(search);
    }

    public ResponseDto<CampaignDetailDto> getDetailCampaign(Long campaignId) throws Exception {
        Campaign findCampaign = campaignRepository.findById(campaignId).orElseThrow(
                () -> new GlobalException(ErrorCode.VALIDATION_FAIL)
        );
        try {
            campaignService.updateSendMessages(findCampaign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CampaignDetailDto campaign = campaignRepository.findByCampaign(campaignId);
        List<CustomerPropertyDto> properties = campaignRepository.findByCustomerGroupToProperties(campaignId);
        List<CopyWriteABDto> copyWriteAB = campaignRepository.findByCopyGroupToCopyWrites(campaignId);
        MessageADto messageADto = campaignRepository.findByMessageACount(campaignId);
        MessageBDto messageBDto = campaignRepository.findByMessageBCount(campaignId);
        campaign.setCustomerProperties(properties);
        campaign.setCopyWriteAB(copyWriteAB);
        campaign.setMessageA(messageADto);
        campaign.setMessageB(messageBDto);
        return ResponseDto.success(campaign);
    }
}
