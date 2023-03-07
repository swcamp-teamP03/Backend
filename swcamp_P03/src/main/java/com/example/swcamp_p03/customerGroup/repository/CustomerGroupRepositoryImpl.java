package com.example.swcamp_p03.customerGroup.repository;

import com.example.swcamp_p03.campaign.entity.QCampaign;
import com.example.swcamp_p03.customerGroup.dto.ExcelFileDto;
import com.example.swcamp_p03.customerGroup.dto.GroupCampaignDto;
import com.example.swcamp_p03.customerGroup.dto.GroupListDto;
import com.example.swcamp_p03.customerGroup.dto.reponse.DetailGroupResponseDto;
import com.example.swcamp_p03.customerGroup.dto.reponse.TotalGroupResponseDto;
import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
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
import static com.example.swcamp_p03.customerGroup.entity.QCustomerGroup.customerGroup;
import static com.example.swcamp_p03.customerGroup.entity.QCustomerProperty.customerProperty;
import static com.example.swcamp_p03.customerGroup.entity.QExcelData.excelData;
import static com.example.swcamp_p03.customerGroup.entity.QExcelFile.excelFile;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class CustomerGroupRepositoryImpl implements CustomerGroupRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public TotalGroupResponseDto findTotalGroup(User user, Pageable pageable) {

        List<GroupListDto> result = jpaQueryFactory.select(Projections.constructor(GroupListDto.class,
                        customerGroup,
                        excelData.excelDataId.count()
                ))
                .from(customerGroup)
                .leftJoin(customerGroup.excelFile, excelFile)
                .leftJoin(excelData).on(excelFile.eq(excelData.excelFile))
                .where(userEq(user))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(customerGroup.customerGroupId)
                .orderBy(customerGroup.favorite.desc())
                .orderBy(groupSort(pageable))
                .fetch();

        Long count = jpaQueryFactory.select(
                        customerGroup.count()
                )
                .from(customerGroup)
                .fetchOne();
        PageImpl<GroupListDto> groupListDtos = new PageImpl<>(result, pageable, count);
        return new TotalGroupResponseDto(groupListDtos.getTotalPages(), groupListDtos.getContent());
    }

    @Override
    public DetailGroupResponseDto findDetailGroup(Long groupId) {
        CustomerGroup findCustomerGroup = jpaQueryFactory.select(
                        customerGroup)
                .from(customerGroup)
                .join(customerGroup.propertyList, customerProperty).fetchJoin()
                .where(customerGroup.customerGroupId.eq(groupId))
                .fetchOne();
        List<GroupCampaignDto> campaignDtoList = jpaQueryFactory.select(Projections.constructor(GroupCampaignDto.class,
                        campaign.campaignName,
                        campaign.sendingDate))
                .from(campaign)
                .join(campaign.customerGroup, customerGroup)
                .fetch();

        ExcelFileDto excelFileDto = jpaQueryFactory.select(Projections.constructor(ExcelFileDto.class,
                        excelFile.excelFileOrgName,
                        excelFile.excelFileSize,
                        excelFile.createdAt,
                        excelData.count()
                ))
                .from(customerGroup)
                .join(customerGroup.excelFile, excelFile)
                .join(excelData).on(excelFile.eq(excelData.excelFile))
                .where(customerGroup.customerGroupId.eq(groupId))
                .fetchOne();
        return new DetailGroupResponseDto(findCustomerGroup, excelFileDto, campaignDtoList);
    }

    @Override
    public TotalGroupResponseDto findSearch(SearchDto searchDto) {
        List<GroupListDto> result = jpaQueryFactory.select(Projections.constructor(GroupListDto.class,
                        customerGroup,
                        excelData.excelDataId.count()
                ))
                .from(customerGroup)
                .leftJoin(customerGroup.excelFile, excelFile).fetchJoin()
                .leftJoin(excelData).on(excelFile.eq(excelData.excelFile))
                .where(userEq(searchDto.getUser()),
                        containKeyword(searchDto.getSearch()),
                        periodDate(searchDto.getStartDate(), searchDto.getEndDate()))
                .offset(searchDto.getPageable().getOffset())
                .limit(searchDto.getPageable().getPageSize())
                .groupBy(customerGroup.customerGroupId)
                .orderBy(customerGroup.favorite.desc(),customerGroup.customerGroupId.desc())
                .fetch();

        Long count = jpaQueryFactory.select(
                        customerGroup.count()
                )
                .from(customerGroup)
                .fetchOne();
        PageImpl<GroupListDto> groupListDtos = new PageImpl<>(result, searchDto.getPageable(), count);
        return new TotalGroupResponseDto(groupListDtos.getTotalPages(), groupListDtos.getContent());
    }

    private OrderSpecifier<?> groupSort(Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                if (order.getProperty().equals("createdAt")) {
                    return new OrderSpecifier<>(direction, customerGroup.createdAt);
                }
            }
        }
        return null;
    }

    private BooleanExpression userEq(User user) {
        return isEmpty(user) ? null : customerGroup.user.eq(user);
    }

    private BooleanExpression containKeyword(String search) {
        return isEmpty(search) ? null : customerGroup.customerGroupName.contains(search);
    }

    private BooleanExpression periodDate(LocalDate startDate, LocalDate endDate) {
        BooleanExpression isGoeStartDate = customerGroup.createdAt.goe(LocalDateTime.of(startDate, LocalTime.MIN));
        BooleanExpression isLoeEndDate = customerGroup.createdAt.loe(LocalDateTime.of(endDate, LocalTime.MAX).withNano(0));
        return Expressions.allOf(isGoeStartDate, isLoeEndDate);
    }

}
