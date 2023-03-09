package com.example.swcamp_p03.campaign.repository;

import com.example.swcamp_p03.campaign.entity.Campaign;
import com.example.swcamp_p03.campaign.entity.SendMessages;
import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SendMessagesRepository extends JpaRepository<SendMessages,Long> {
    Optional<SendMessages> findByUniqueUrl(String uniqueUrl);
    List<SendMessages> findAllByCampaign(Campaign campaign);

}
