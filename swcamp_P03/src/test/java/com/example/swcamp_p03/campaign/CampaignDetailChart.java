package com.example.swcamp_p03.campaign;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static com.example.swcamp_p03.campaign.entity.QCampaign.*;
import static com.example.swcamp_p03.campaign.entity.QCampaignMessage.campaignMessage;
import static com.example.swcamp_p03.campaign.entity.QSendMessages.*;

@SpringBootTest
public class CampaignDetailChart {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Test
    void test() {
        Tuple tupleA = jpaQueryFactory.select(
                        JPAExpressions.select(sendMessages.count())
                                .from(campaign)
                                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
                                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
                                .where(campaign.campaignId.eq(1L), campaignMessage.messageSection.eq("A"), sendMessages.campaignMessage.eq(campaignMessage)),
                        sendMessages.visitedTime.count(),
                        sendMessages.count())
                .from(campaign)
                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
                .where(campaign.campaignId.eq(1L), campaignMessage.messageSection.eq("A"), sendMessages.campaignMessage.eq(campaignMessage), sendMessages.sendState.eq("발송성공"))
                .fetchOne();
        Tuple tupleB = jpaQueryFactory.select(
                        JPAExpressions.select(sendMessages.count())
                                .from(campaign)
                                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
                                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
                                .where(campaign.campaignId.eq(1L), campaignMessage.messageSection.eq("B"), sendMessages.campaignMessage.eq(campaignMessage)),
                        sendMessages.visitedTime.count(),
                        sendMessages.count())
                .from(campaign)
                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
                .where(campaign.campaignId.eq(1L), campaignMessage.messageSection.eq("B"), sendMessages.campaignMessage.eq(campaignMessage), sendMessages.sendState.eq("발송성공"))
                .fetchOne();

        LocalDate startDate = LocalDate.of(2023,3,11);
        LocalDate endDate = startDate.plusDays(14);
        jpaQueryFactory.select(
                        sendMessages.visitedTime.count()
                )
                .from(campaign)
                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
                .where(periodDate(startDate, endDate))
                .groupBy(sendMessages.visitedDate);
    }

    private BooleanExpression periodDate(LocalDate startDate, LocalDate endDate) {
        BooleanExpression isGoeStartDate = sendMessages.visitedDate.goe(startDate);
        BooleanExpression isLoeEndDate = sendMessages.visitedDate.loe(endDate);
        return Expressions.allOf(isGoeStartDate, isLoeEndDate);
    }
}
