package com.example.swcamp_p03.campaign.repository;

import com.example.swcamp_p03.campaign.dto.CampaignDto;
import com.example.swcamp_p03.user.entity.User;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.example.swcamp_p03.campaign.entity.QCampaign.campaign;
import static com.example.swcamp_p03.campaign.entity.QSendMessages.sendMessages;
import static com.example.swcamp_p03.customerGroup.entity.QCustomerGroup.customerGroup;

@RequiredArgsConstructor
public class CampaignRepositoryImpl implements CampaignRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CampaignDto> findCampaign(User user, Pageable pageable) {
        return jpaQueryFactory.select(Projections.constructor(CampaignDto.class,
                                campaign.favorite,
                                campaign.campaignName,
                                campaign.messageType,
                                campaign.createdAt,
                                campaign.sendingDate,
                                sendMessages.visitedTime.count(),
                                sendMessages.campaign.count()
                        )
                )
                .from(campaign)
                .leftJoin(sendMessages).on(campaign.eq(sendMessages.campaign))
                .where(campaign.user.eq(user))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(campaign)
                .orderBy(campaign.favorite.desc())
                .orderBy(groupSort(pageable))
                .fetch();
    }

    @Override
    public Long countCampaign(User user) {
        return jpaQueryFactory.select(campaign.count())
                .from(campaign)
                .where(campaign.user.eq(user))
                .fetchOne();
    }

    private OrderSpecifier<?> groupSort(Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                if (order.getProperty().equals("createdAt")) {
                    return new OrderSpecifier<>(direction, campaign.createdAt);
                }
            }
        }
        return null;
    }
}
