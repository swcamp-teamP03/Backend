package com.example.swcamp_p03.campaign.repository;

import com.example.swcamp_p03.campaign.dto.*;
import com.example.swcamp_p03.campaign.dto.response.CampaignDetailDto;
import com.example.swcamp_p03.campaign.dto.response.TotalCampaignResponseDto;
import com.example.swcamp_p03.customerGroup.dto.SearchDto;
import com.example.swcamp_p03.user.entity.User;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
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
import static com.example.swcamp_p03.campaign.entity.QCampaignMessage.campaignMessage;
import static com.example.swcamp_p03.campaign.entity.QSendMessages.sendMessages;
import static com.example.swcamp_p03.copyGroup.entity.QCopyGroup.copyGroup;
import static com.example.swcamp_p03.customerGroup.entity.QCustomerGroup.customerGroup;
import static com.example.swcamp_p03.customerGroup.entity.QExcelFile.excelFile;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class CampaignRepositoryImpl implements CampaignRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public TotalCampaignResponseDto findTotalCampaign(User user, Pageable pageable) {
        List<CampaignDto> result = jpaQueryFactory.select(Projections.constructor(CampaignDto.class,
                                campaign.favorite,
                                campaign.messageType,
                                campaign.campaignName,
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
        return new TotalCampaignResponseDto(count,campaignDtos);

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
                .orderBy(campaign.favorite.desc(), campaign.campaignId.desc())
                .fetch();
        Long count = jpaQueryFactory.select(campaign.count())
                .from(campaign)
                .where(userEq(searchDto.getUser()))
                .fetchOne();
        PageImpl<CampaignDto> campaignDtos = new PageImpl<>(result, searchDto.getPageable(), count);
        return new TotalCampaignResponseDto(count, campaignDtos);

    }

    @Override
    public CampaignDetailDto findByCampaign(Long campaignId) {
        return jpaQueryFactory.select(Projections.constructor(CampaignDetailDto.class,
                        campaign.createdAt,
                        campaign.sendingDate,
                        sendMessages.campaign.count(),
                        campaign.messageType,
                        customerGroup.customerGroupName,
                        excelFile.excelFileOrgName,
                        copyGroup.coupGroupName,
                        campaign.comment
                ))
                .from(campaign)
                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
                .join(campaign.customerGroup, customerGroup)
                .join(campaign.copyGroup, copyGroup)
                .where(campaign.campaignId.eq(campaignId))
                .fetchOne();
    }

    @Override
    public List<CustomerPropertyDto> findByCustomerGroupToProperties(Long campaignId) {
        return null;
    }

    @Override
    public List<CopyWriteABDto> findByCopyGroupToCopyWrites(Long campaignId) {
        return jpaQueryFactory.select(Projections.constructor(CopyWriteABDto.class,
                        campaignMessage.messageSection,
                        campaignMessage.message))
                .from(campaign)
                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
                .where(campaign.campaignId.eq(campaignId))
                .fetch();
    }

    @Override
    public MessageADto findByMessageACount(Long campaignId) {
        return jpaQueryFactory.select(Projections.constructor(MessageADto.class,
                        JPAExpressions.select(sendMessages.count())
                                .from(campaign)
                                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
                                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
                                .where(campaign.campaignId.eq(campaignId), campaignMessage.messageSection.eq("A"), sendMessages.campaignMessage.eq(campaignMessage)),
                        sendMessages.visitedTime.count(),
                        sendMessages.count()))
                .from(campaign)
                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
                .where(campaign.campaignId.eq(campaignId), campaignMessage.messageSection.eq("A"), sendMessages.campaignMessage.eq(campaignMessage), sendMessages.sendState.eq("발송성공"))
                .fetchOne();
    }

    @Override
    public MessageBDto findByMessageBCount(Long campaignId) {
        return jpaQueryFactory.select(Projections.constructor(MessageBDto.class,
                        JPAExpressions.select(sendMessages.count())
                                .from(campaign)
                                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
                                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
                                .where(campaign.campaignId.eq(campaignId), campaignMessage.messageSection.eq("B"), sendMessages.campaignMessage.eq(campaignMessage)),
                        sendMessages.visitedTime.count(),
                        sendMessages.count()))
                .from(campaign)
                .join(sendMessages).on(campaign.eq(sendMessages.campaign))
                .join(campaignMessage).on(campaign.eq(campaignMessage.campaign))
                .where(campaign.campaignId.eq(campaignId), campaignMessage.messageSection.eq("B"), sendMessages.campaignMessage.eq(campaignMessage), sendMessages.sendState.eq("발송성공"))
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
