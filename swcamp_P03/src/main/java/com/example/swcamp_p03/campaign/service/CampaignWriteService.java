package com.example.swcamp_p03.campaign.service;

import com.example.swcamp_p03.campaign.dto.request.CommentRequestDto;
import com.example.swcamp_p03.campaign.entity.Campaign;
import com.example.swcamp_p03.campaign.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CampaignWriteService {
    private final CampaignRepository campaignRepository;

    @Transactional
    public void favoriteCheck(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId).get();
        campaign.setFavorite(!campaign.getFavorite());
    }

    @Transactional
    public void writeComment(Long campaignId, CommentRequestDto requestDto) {
        Campaign campaign = campaignRepository.findById(campaignId).get();
        campaign.setComment(requestDto.getComment());
    }
}
