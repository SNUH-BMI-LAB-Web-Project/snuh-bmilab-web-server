package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.dto.condition.ProjectFilterCondition;
import com.bmilab.backend.domain.project.dto.query.GetAllProjectsQueryResult;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.entity.QProject;
import com.bmilab.backend.domain.project.entity.QProjectParticipant;
import com.bmilab.backend.domain.project.enums.ProjectParticipantType;
import com.bmilab.backend.domain.user.entity.QUser;
import com.bmilab.backend.domain.user.entity.User;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GetAllProjectsQueryResult> findAllBySearch(String keyword, Pageable pageable,
                                                           ProjectFilterCondition condition) {

        QProject project = QProject.project;
        QProjectParticipant participant = QProjectParticipant.projectParticipant;
        QUser user = QUser.user;

        Expression<List<User>> listNullExpression = Expressions.constant(Collections.emptyList());

        BooleanExpression titleContains = keyword != null ? project.title.containsIgnoreCase(keyword) : null;

        BooleanExpression leaderNameContains = keyword != null
                ? JPAExpressions.selectOne()
                .from(participant)
                .join(participant.user, user)
                .where(
                        participant.project.eq(project),
                        participant.type.eq(ProjectParticipantType.LEADER),
                        user.name.containsIgnoreCase(keyword)
                ).exists()
                : null;

        BooleanExpression leaderFilter = condition.leaderId() != null
                ? JPAExpressions.selectOne()
                .from(participant)
                .join(participant.user, user)
                .where(
                        participant.user.id.eq(condition.leaderId())
                ).exists()
                : null;

        BooleanExpression statusFilter = condition.status() != null ? project.status.eq(condition.status()) : null;

        BooleanExpression categoryFilter =
                condition.category() != null ? project.category.eq(condition.category()) : null;

        List<GetAllProjectsQueryResult> results = queryFactory
                .select(Projections.constructor(
                        GetAllProjectsQueryResult.class,
                        project.id,
                        project.title,
                        project.category,
                        project.startDate,
                        project.endDate,
                        project.pi,
                        project.practicalProfessor,
                        listNullExpression,
                        ExpressionUtils.as(
                                JPAExpressions.select(participant.count())
                                        .from(participant)
                                        .where(participant.project.eq(project)),
                                "participantCount"
                        ),
                        project.status,
                        project.isPrivate
                ))
                .from(project)
                .where(
                        Expressions.anyOf(titleContains, leaderNameContains),
                        statusFilter,
                        categoryFilter,
                        leaderFilter
                )
                .orderBy(getOrderSpecifiers(pageable, Project.class, "project", project.createdAt.desc()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = Optional.ofNullable(
                queryFactory
                        .select(project.count())
                        .from(project)
                        .where(
                                Expressions.anyOf(titleContains, leaderNameContains),
                                statusFilter,
                                categoryFilter,
                                leaderFilter
                        )
                        .fetchOne()
        ).orElse(0L);

        List<Long> projectIds = results.stream()
                .map(GetAllProjectsQueryResult::getProjectId)
                .toList();

        List<Tuple> leaderResults = queryFactory.select(participant.project.id, user)
                .from(participant)
                .join(participant.user, user)
                .where(participant.project.id.in(projectIds), participant.type.eq(ProjectParticipantType.LEADER))
                .fetch();

        Map<Long, List<User>> leadersMap = leaderResults.stream()
                .filter(it -> it.get(0, Long.class) != null && it.get(1, User.class) != null)
                .collect(Collectors.groupingBy(
                        it -> it.get(0, Long.class),
                        Collectors.mapping(it -> it.get(1, User.class), Collectors.toList())
                ));

        results.forEach(it ->
                it.setLeaders(
                        leadersMap.getOrDefault(it.getProjectId(), List.of())
                )
        );

        return PageableExecutionUtils.getPage(results, pageable, () -> count);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, Class<?> clazz, String alias, OrderSpecifier<?> defaultSort) {
        PathBuilder<?> pathBuilder = new PathBuilder<>(clazz, alias);

        if (!pageable.getSort().isSorted()) {
            return new OrderSpecifier[]{ defaultSort };
        }

        return pageable.getSort().stream()
                .map(order -> new OrderSpecifier<>(
                        order.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(order.getProperty(), Comparable.class)
                ))
                .toArray(OrderSpecifier[]::new);
    }
}
