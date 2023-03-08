package com.example.swcamp_p03.campaign.repository;

import com.example.swcamp_p03.campaign.dto.CampaignDto;
import com.example.swcamp_p03.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CampaignRepositoryCustom {
    List<CampaignDto> findCampaign(User user, Pageable pageable);
    Long countCampaign(User user);

}
