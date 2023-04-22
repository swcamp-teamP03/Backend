package com.example.swcamp_p03.user.repository;

import com.example.swcamp_p03.user.dto.UserDataDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.swcamp_p03.copyGroup.entity.QCopyGroup.copyGroup;
import static com.example.swcamp_p03.user.entity.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<UserDataDto> getUserDataAll() {
        return jpaQueryFactory.select(Projections.constructor(UserDataDto.class,
                        user.createdAt,
                        user.visitedTime,
                        user.email,
                        user.username,
                        user.phoneNumber,
                        user.company,
                        JPAExpressions.select(copyGroup.count())
                                .from(copyGroup)
                                .where(copyGroup.user.eq(user)),
                        user.campaignTestCount
                ))
                .from(user)
                .fetch();
    }
}
