package com.example.swcamp_p03.campaign.repository;

import com.example.swcamp_p03.campaign.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long>,CampaignRepositoryCustom {
}
