package com.bmilab.backend.domain.user.repository;

import com.bmilab.backend.domain.user.dto.request.UserSubAffiliationRequest;
import com.bmilab.backend.domain.user.entity.QUserSubAffiliation;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserSubAffiliation;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserSubAffiliationRepositoryCustomImpl implements UserSubAffiliationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserSubAffiliation> findExistsAsEntity(User user, List<UserSubAffiliationRequest> keys) {

        QUserSubAffiliation usa = QUserSubAffiliation.userSubAffiliation;

        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if (keys.isEmpty()) {
            return List.of();
        }


        keys.forEach((key) -> conditionBuilder.or(usa.organization.eq(key.organization())
                .and(usa.organization.eq(key.department()))
                .and(usa.department.eq(key.position()))));

        return queryFactory.select(usa).from(usa).where(usa.user.eq(user), conditionBuilder).fetch();
    }
}
