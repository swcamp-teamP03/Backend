package com.example.swcamp_p03.campaign;

import com.example.swcamp_p03.campaign.dataInit.CampaignDataInit;
import com.example.swcamp_p03.campaign.repository.CampaignMessageRepository;
import com.example.swcamp_p03.campaign.repository.CampaignRepository;
import com.example.swcamp_p03.campaign.repository.SendMessagesRepository;
import com.example.swcamp_p03.customerGroup.dto.GroupListDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.swcamp_p03.campaign.entity.QCampaign.campaign;
import static com.example.swcamp_p03.campaign.entity.QSendMessages.sendMessages;

@SpringBootTest
public class CampaignList {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private CampaignMessageRepository campaignMessageRepository;
    @Autowired
    private SendMessagesRepository sendMessagesRepository;

    @Test
    void test() {
//        int page = 1;
//        int size = 5;
//        Pageable pageable = PageRequest.of(page, size);
//        List<Tuple> fetch = jpaQueryFactory.select(
//                        campaign.favorite,
//                        campaign.campaignName,
//                        campaign.messageType,
//                        campaign.createdAt,
//                        campaign.sendingDate,
//                        sendMessages.campaign.count(),
//                        sendMessages.visitedTime.count()
//
//                )
//                .from(campaign)
//                .leftJoin(sendMessages).on(campaign.eq(sendMessages.campaign))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .groupBy(campaign)
//                .fetch();
//        System.out.println("fetch = " + fetch);
        LocalDate now = LocalDate.now();
        LocalDate to = LocalDate.of(2023, 4, 1);
        System.out.println(now.compareTo(to));
    }
}
