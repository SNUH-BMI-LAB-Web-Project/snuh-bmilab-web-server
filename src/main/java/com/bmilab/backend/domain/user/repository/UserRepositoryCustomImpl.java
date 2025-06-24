package com.bmilab.backend.domain.user.repository;

import com.bmilab.backend.domain.leave.entity.QLeave;
import com.bmilab.backend.domain.leave.entity.QUserLeave;
import com.bmilab.backend.domain.user.dto.query.UserDetailQueryResult;
import com.bmilab.backend.domain.user.dto.query.UserInfoQueryResult;
import com.bmilab.backend.domain.user.entity.QUser;
import com.bmilab.backend.domain.user.entity.QUserInfo;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<UserDetailQueryResult> findUserDetailsById(Long userId) {
        QUser user = QUser.user;
        QUserInfo userInfo = QUserInfo.userInfo;
        QUserLeave userLeave = QUserLeave.userLeave;
        QLeave leave = QLeave.leave;

        Map<Long, UserDetailQueryResult> result = queryFactory
                .from(user)
                .innerJoin(userInfo).on(userInfo.user.eq(user))
                .innerJoin(userLeave).on(userLeave.user.eq(user))
                .leftJoin(leave).on(leave.user.eq(user))
                .where(user.id.eq(userId))
                .transform(
                        GroupBy.groupBy(user.id).as(
                                Projections.constructor(
                                        UserDetailQueryResult.class,
                                        user,
                                        userInfo,
                                        userLeave,
                                        GroupBy.list(leave)
                                )
                        )
                );

        return Optional.ofNullable(result.get(userId));
    }

    @Override
    public Page<UserInfoQueryResult> findAllUserInfosPagination(Pageable pageable) {
        QUser user = QUser.user;
        QUserInfo userInfo = QUserInfo.userInfo;

        List<UserInfoQueryResult> results = queryFactory
                .select(Projections.constructor(
                        UserInfoQueryResult.class,
                        user,
                        userInfo
                ))
                .from(user)
                .innerJoin(userInfo).on(userInfo.user.eq(user))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = Optional.ofNullable(
                queryFactory
                        .select(user.count())
                        .from(user)
                        .fetchOne()
        ).orElse(0L);

        return PageableExecutionUtils.getPage(results, pageable, () -> count);
    }
}
