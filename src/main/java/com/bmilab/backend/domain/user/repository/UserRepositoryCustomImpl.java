package com.bmilab.backend.domain.user.repository;


import com.bmilab.backend.domain.leave.entity.QUserLeave;
import com.bmilab.backend.domain.projectcategory.entity.ProjectCategory;
import com.bmilab.backend.domain.projectcategory.entity.QProjectCategory;
import com.bmilab.backend.domain.user.dto.query.UserDetailQueryResult;
import com.bmilab.backend.domain.user.dto.query.UserInfoQueryResult;
import com.bmilab.backend.domain.user.dto.query.UserCondition;
import com.bmilab.backend.domain.user.entity.QUser;
import com.bmilab.backend.domain.user.entity.QUserInfo;
import com.bmilab.backend.domain.user.entity.QUserProjectCategory;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.entity.UserInfo;
import com.bmilab.backend.domain.user.enums.UserPosition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
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

    @Override
    public Page<UserInfoQueryResult> searchUserInfos(UserCondition condition, Pageable pageable) {
        QUser user = QUser.user;
        QUserInfo userInfo = QUserInfo.userInfo;
        QUserProjectCategory userProjectCategory = QUserProjectCategory.userProjectCategory;
        QProjectCategory category = QProjectCategory.projectCategory;

        BooleanBuilder conditionBuilder = new BooleanBuilder();
        String filterBy = Optional.ofNullable(condition.getFilterBy()).map(String::trim).map(String::toLowerCase).orElse(null);
        String filterValue = Optional.ofNullable(condition.getFilterValue()).map(String::trim).orElse(null);
        UserPosition position = UserPosition.fromString(filterValue);

        BooleanExpression nameContains = (filterValue == null || filterValue.isBlank()) ? null : user.name.containsIgnoreCase(filterValue);
        BooleanExpression emailContains = (filterValue == null || filterValue.isBlank()) ? null : user.email.containsIgnoreCase(filterValue);
        BooleanExpression departmentContains = (filterValue == null || filterValue.isBlank()) ? null : user.department.containsIgnoreCase(filterValue);
        BooleanExpression organizationContains = (filterValue == null || filterValue.isBlank()) ? null : user.organization.containsIgnoreCase(filterValue);
        BooleanExpression affiliationEquals = (filterValue == null || filterValue.isBlank() || position == null) ?
                null : user.position.eq(position);
        BooleanExpression categoryContains = (filterValue == null || filterValue.isBlank()) ? null : category.name.containsIgnoreCase(filterValue);
        BooleanExpression seatNumberContains = (filterValue == null || filterValue.isBlank()) ? null : userInfo.seatNumber.containsIgnoreCase(filterValue);
        BooleanExpression phoneNumberContains = (filterValue == null || filterValue.isBlank()) ? null : userInfo.phoneNumber.containsIgnoreCase(filterValue);

        if (filterBy != null && !filterBy.isBlank() &&
                filterValue != null && !filterValue.isBlank()) {
            if (filterBy.equalsIgnoreCase("all")) {
                conditionBuilder.andAnyOf(
                        nameContains,
                        emailContains,
                        departmentContains,
                        organizationContains,
                        affiliationEquals,
                        categoryContains,
                        seatNumberContains,
                        phoneNumberContains
                );
            } else {
                switch (filterBy) {
                    case "name" -> conditionBuilder.and(nameContains);
                    case "email" -> conditionBuilder.and(emailContains);
                    case "organization" -> conditionBuilder.and(organizationContains);
                    case "department" -> conditionBuilder.and(departmentContains);
                    case "position" -> conditionBuilder.and(positionEquals);
                    case "projectname" -> conditionBuilder.and(categoryContains);
                    case "seatnumber" -> conditionBuilder.and(seatNumberContains);
                    case "phonenumber" -> conditionBuilder.and(phoneNumberContains);
                }
            }
        }

        OrderSpecifier<?> orderSpecifier = "desc".equalsIgnoreCase(condition.getDirection())
                ? user.name.desc()
                : user.name.asc();

        List<Long> userIds = queryFactory
                .select(user.id)
                .from(user)
                .leftJoin(userInfo).on(userInfo.user.eq(user))
                .leftJoin(userProjectCategory).on(userProjectCategory.user.eq(user))
                .leftJoin(category).on(userProjectCategory.category.eq(category))
                .where(conditionBuilder)
                .groupBy(user.id, user.name)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetch();

        if (userIds.isEmpty()) {
            return PageableExecutionUtils.getPage(Collections.emptyList(), pageable, () -> 0L);
        }

        List<Tuple> rows = queryFactory
                .select(user, userInfo, category)
                .from(user)
                .leftJoin(userInfo).on(userInfo.user.eq(user))
                .leftJoin(userProjectCategory).on(userProjectCategory.user.eq(user))
                .leftJoin(category).on(userProjectCategory.category.eq(category))
                .where(user.id.in(userIds))
                .orderBy(orderSpecifier)
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
                resultMap.put(userId, new UserInfoQueryResult(u, ui, categories));
            } else {
                if (c != null && !existing.getCategories().contains(c)) {
                    existing.getCategories().add(c);
                }
            }
        });

        List<UserInfoQueryResult> results = new ArrayList<>(resultMap.values());

        Long total = queryFactory
                .select(user.countDistinct())
                .from(user)
                .leftJoin(userInfo).on(userInfo.user.eq(user))
                .leftJoin(userProjectCategory).on(userProjectCategory.user.eq(user))
                .leftJoin(category).on(userProjectCategory.category.eq(category))
                .where(conditionBuilder)
                .fetchOne();

        return PageableExecutionUtils.getPage(results, pageable, () -> total != null ? total : 0L);
    }

}