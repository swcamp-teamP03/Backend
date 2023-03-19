package com.example.swcamp_p03.copyGroup.repository;

import com.example.swcamp_p03.copyGroup.dto.CopyGroupListElementDto;
import com.example.swcamp_p03.copyGroup.dto.response.CopyGroupListResponseDto;
import com.example.swcamp_p03.common.dto.SearchDto;
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

import static com.example.swcamp_p03.copyGroup.entity.QCopyGroup.copyGroup;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class CopyGroupRepositoryImpl implements CopyGroupRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public CopyGroupListResponseDto findTotalCopyGroup(User user, Pageable pageable) {
        List<CopyGroupListElementDto> result = jpaQueryFactory.select(Projections.constructor(CopyGroupListElementDto.class,
                        copyGroup.copyGroupId,
                        copyGroup.createdAt,
                        copyGroup.favorite,
                        copyGroup.coupGroupName))
                .from(copyGroup)
                .where(userEq(user))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(copyGroup.favorite.desc())
                .orderBy(groupSort(pageable))
                .fetch();

        Long count = jpaQueryFactory.select(
                        copyGroup.count()
                )
                .from(copyGroup)
                .where(userEq(user))
                .fetchOne();
        PageImpl<CopyGroupListElementDto> copyGroupList = new PageImpl<>(result, pageable, count);
        return new CopyGroupListResponseDto(count, copyGroupList);
    }

    @Override
    public CopyGroupListResponseDto findSearch(SearchDto searchDto) {
        List<CopyGroupListElementDto> result = jpaQueryFactory.select(Projections.constructor(CopyGroupListElementDto.class,
                        copyGroup.copyGroupId,
                        copyGroup.createdAt,
                        copyGroup.favorite,
                        copyGroup.coupGroupName))
                .from(copyGroup)
                .where(userEq(searchDto.getUser()),
                        containKeyword(searchDto.getSearch()),
                        periodDate(searchDto.getStartDate(), searchDto.getEndDate()))
                .offset(searchDto.getPageable().getOffset())
                .limit(searchDto.getPageable().getPageSize())
                .orderBy(copyGroup.favorite.desc())
                .orderBy(copyGroup.favorite.desc(),copyGroup.copyGroupId.desc())
                .fetch();

        Long count = jpaQueryFactory.select(
                        copyGroup.count()
                )
                .from(copyGroup)
                .where(userEq(searchDto.getUser()))
                .fetchOne();
        PageImpl<CopyGroupListElementDto> copyGroupList = new PageImpl<>(result, searchDto.getPageable(), count);
        return new CopyGroupListResponseDto(count, copyGroupList);
    }

    private OrderSpecifier<?> groupSort(Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                if (order.getProperty().equals("createdAt")) {
                    return new OrderSpecifier<>(direction, copyGroup.createdAt);
                }
            }
        }
        return null;
    }

    private BooleanExpression userEq(User user) {
        return isEmpty(user) ? null : copyGroup.user.eq(user);
    }

    private BooleanExpression containKeyword(String search) {
        return isEmpty(search) ? null : copyGroup.coupGroupName.contains(search);
    }

    private BooleanExpression periodDate(LocalDate startDate, LocalDate endDate) {
        BooleanExpression isGoeStartDate = copyGroup.createdAt.goe(LocalDateTime.of(startDate, LocalTime.MIN));
        BooleanExpression isLoeEndDate = copyGroup.createdAt.loe(LocalDateTime.of(endDate, LocalTime.MAX).withNano(0));
        return Expressions.allOf(isGoeStartDate, isLoeEndDate);
    }
}
