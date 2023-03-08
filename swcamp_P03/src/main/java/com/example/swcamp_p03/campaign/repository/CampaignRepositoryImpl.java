package com.example.swcamp_p03.campaign.repository;

import com.example.swcamp_p03.campaign.dto.CampaignDto;
import com.example.swcamp_p03.campaign.dto.response.TotalCampaignResponseDto;
import com.example.swcamp_p03.customerGroup.dto.SearchDto;
import com.example.swcamp_p03.user.entity.User;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.example.swcamp_p03.campaign.entity.QCampaign.campaign;
import static com.example.swcamp_p03.campaign.entity.QSendMessages.sendMessages;
import static com.example.swcamp_p03.customerGroup.entity.QCustomerGroup.customerGroup;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class CampaignRepositoryImpl implements CampaignRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public TotalCampaignResponseDto findTotalCampaign(User user, Pageable pageable) {
        List<CampaignDto> result = jpaQueryFactory.select(Projections.constructor(CampaignDto.class,
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
                .where(userEq(user))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(campaign)
                .orderBy(campaign.favorite.desc(), groupSort(pageable))
                .fetch();

        Long count = jpaQueryFactory.select(campaign.count())
                .from(campaign)
                .where(campaign.user.eq(user))
                .fetchOne();

        PageImpl<CampaignDto> campaignDtos = new PageImpl<>(result, pageable, count);
        return new TotalCampaignResponseDto(campaignDtos);

    }

    @Override
    public TotalCampaignResponseDto findSearch(SearchDto searchDto) {
        List<CampaignDto> result = jpaQueryFactory.select(Projections.constructor(CampaignDto.class,
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
                .where(userEq(searchDto.getUser()),
                        containKeyword(searchDto.getSearch()),
                        periodDate(searchDto.getStartDate(), searchDto.getEndDate()))
                .offset(searchDto.getPageable().getOffset())
                .limit(searchDto.getPageable().getPageSize())
                .groupBy(campaign)
                .orderBy(campaign.favorite.desc(), groupSort(searchDto.getPageable()))
                .fetch();
        Long count = jpaQueryFactory.select(campaign.count())
                .from(campaign)
                .where(userEq(searchDto.getUser()))
                .fetchOne();
        PageImpl<CampaignDto> campaignDtos = new PageImpl<>(result, searchDto.getPageable(), count);
        return new TotalCampaignResponseDto(campaignDtos);

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

    private BooleanExpression userEq(User user) {
        return isEmpty(user) ? null : campaign.user.eq(user);
    }

    private BooleanExpression containKeyword(String search) {
        return isEmpty(search) ? null : campaign.campaignName.contains(search);
    }

    private BooleanExpression periodDate(LocalDate startDate, LocalDate endDate) {
        BooleanExpression isGoeStartDate = campaign.createdAt.goe(LocalDateTime.of(startDate, LocalTime.MIN));
        BooleanExpression isLoeEndDate = campaign.createdAt.loe(LocalDateTime.of(endDate, LocalTime.MAX).withNano(0));
        return Expressions.allOf(isGoeStartDate, isLoeEndDate);
    }
}
