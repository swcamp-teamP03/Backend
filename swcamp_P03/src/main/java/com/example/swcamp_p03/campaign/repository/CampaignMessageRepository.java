package com.example.swcamp_p03.campaign.repository;

import com.example.swcamp_p03.campaign.entity.CampaignMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignMessageRepository extends JpaRepository<CampaignMessage,Long> {
}
