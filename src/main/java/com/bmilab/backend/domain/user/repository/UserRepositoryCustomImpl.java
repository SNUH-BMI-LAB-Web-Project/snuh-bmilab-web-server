package com.bmilab.backend.domain.user.repository;


import com.bmilab.backend.domain.leave.entity.QUserLeave;
import com.bmilab.backend.domain.projectcategory.entity.ProjectCategory;
import com.bmilab.backend.domain.projectcategory.entity.QProjectCategory;
import com.bmilab.backend.domain.user.dto.query.UserDetailQueryResult;
import com.bmilab.backend.domain.user.dto.query.UserInfoQueryResult;
import com.bmilab.backend.domain.user.entity.QUser;
import com.bmilab.backend.domain.user.entity.QUserInfo;
import com.bmilab.backend.domain.user.entity.QUserProjectCategory;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserInfo;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

        UserDetailQueryResult result = queryFactory
                .select(Projections.constructor(
                        UserDetailQueryResult.class,
                        user,
                        userLeave,
                        userInfo
                ))
                .from(user)
                .innerJoin(userInfo).on(userInfo.user.eq(user))
                .innerJoin(userLeave).on(userLeave.user.eq(user))
                .where(user.id.eq(userId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

//    @Override
//    public Page<UserInfoQueryResult> findAllUserInfosPagination(Pageable pageable) {
//        QUser user = QUser.user;
//        QUserInfo userInfo = QUserInfo.userInfo;
//
//        List<UserInfoQueryResult> results = queryFactory
//                .select(Projections.constructor(
//                        UserInfoQueryResult.class,
//                        user,
//                        userInfo
//                ))
//                .from(user)
//                .innerJoin(userInfo).on(userInfo.user.eq(user))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        Long count = Optional.ofNullable(
//                queryFactory
//                        .select(user.count())
//                        .from(user)
//                        .fetchOne()
//        ).orElse(0L);
//
//        return PageableExecutionUtils.getPage(results, pageable, () -> count);
//    }

    @Override
    public Page<UserInfoQueryResult> findAllUserInfosPagination(Pageable pageable) {
        QUser user = QUser.user;
        QUserInfo userInfo = QUserInfo.userInfo;
        QUserProjectCategory userProjectCategory = QUserProjectCategory.userProjectCategory;
        QProjectCategory category = QProjectCategory.projectCategory;

        List<Tuple> rows = queryFactory
                .select(user, userInfo, category)
                .from(user)
                .innerJoin(userInfo).on(userInfo.user.eq(user))
                .leftJoin(userProjectCategory).on(userProjectCategory.user.eq(user))
                .leftJoin(category).on(userProjectCategory.category.eq(category))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Map<Long, UserInfoQueryResult> resultMap = new LinkedHashMap<>();

        rows.forEach(row -> {
            User u = row.get(user);
            UserInfo ui = row.get(userInfo);
            ProjectCategory c = row.get(category);

            Long userId = Objects.requireNonNull(u).getId();
            UserInfoQueryResult existing = resultMap.get(userId);

            if (existing == null) {
                List<ProjectCategory> categories = new ArrayList<>();
                if (c != null) categories.add(c);

                UserInfoQueryResult result = new UserInfoQueryResult(u, ui, categories);

                resultMap.put(userId, result);
            } else {
                if (c != null && !existing.getCategories().contains(c)) {
                    existing.getCategories().add(c);
                }
            }
        });

        List<UserInfoQueryResult> results = new ArrayList<>(resultMap.values());

        Long count = Optional.ofNullable(
                queryFactory
                        .select(user.count())
                        .from(user)
                        .fetchOne()
        ).orElse(0L);

        return PageableExecutionUtils.getPage(results, pageable, () -> count);
    }
}
