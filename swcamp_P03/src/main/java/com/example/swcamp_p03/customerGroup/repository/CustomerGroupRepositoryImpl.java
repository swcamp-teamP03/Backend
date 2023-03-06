package com.example.swcamp_p03.customerGroup.repository;

import com.example.swcamp_p03.customerGroup.dto.ExcelFileDto;
import com.example.swcamp_p03.customerGroup.dto.GroupListDto;
import com.example.swcamp_p03.customerGroup.dto.reponse.DetailGroupResponseDto;
import com.example.swcamp_p03.customerGroup.dto.reponse.TotalGroupResponseDto;
import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
import com.example.swcamp_p03.user.entity.User;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.swcamp_p03.customerGroup.entity.QCustomerGroup.customerGroup;
import static com.example.swcamp_p03.customerGroup.entity.QCustomerProperty.customerProperty;
import static com.example.swcamp_p03.customerGroup.entity.QExcelData.excelData;
import static com.example.swcamp_p03.customerGroup.entity.QExcelFile.excelFile;

@RequiredArgsConstructor
public class CustomerGroupRepositoryImpl implements CustomerGroupRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public TotalGroupResponseDto findTotalGroup(User user, Pageable pageable) {

        List<GroupListDto> result = jpaQueryFactory.select(Projections.constructor(GroupListDto.class,
                        customerGroup,
                        excelData.excelDataId.count()
                ))
                .from(customerGroup)
                .leftJoin(customerGroup.excelFile, excelFile).fetchJoin()
                .leftJoin(excelData).on(excelFile.eq(excelData.excelFile))
                .where(customerGroup.user.eq(user))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(customerGroup.customerGroupId)
                .orderBy(customerGroup.createdAt.desc())
                .fetch();
        Long count = jpaQueryFactory.select(
                        customerGroup.count()
                )
                .from(customerGroup)
                .fetchOne();
        return new TotalGroupResponseDto(count, result);
    }

    @Override
    public DetailGroupResponseDto findDetailGroup(Long groupId) {
        CustomerGroup findGroup = jpaQueryFactory.select(
                        customerGroup)
                .from(customerGroup)
                .join(customerGroup.propertyList, customerProperty).fetchJoin()
                .where(customerGroup.customerGroupId.eq(groupId))
                .fetchOne();

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
        return new DetailGroupResponseDto(findGroup, excelFileDto);
    }
}
