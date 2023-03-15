package com.example.swcamp_p03.campaign.repository;

import com.example.swcamp_p03.campaign.entity.Campaign;
import com.example.swcamp_p03.campaign.entity.CampaignMessage;
import com.example.swcamp_p03.campaign.entity.SendMessages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignMessageRepository extends JpaRepository<CampaignMessage,Long> {
    List<CampaignMessage> findAllByCampaign(Campaign campaign);

}
