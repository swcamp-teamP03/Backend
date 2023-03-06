package com.example.swcamp_p03.campaign.repository;

import com.example.swcamp_p03.campaign.entity.SendMessages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendMessagesRepository extends JpaRepository<SendMessages,Long> {
}
