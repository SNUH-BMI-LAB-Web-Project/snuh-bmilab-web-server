package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.dto.query.GetAllProjectsQueryResult;
import com.bmilab.backend.domain.project.entity.QProject;
import com.bmilab.backend.domain.project.entity.QProjectParticipant;
import com.bmilab.backend.domain.user.entity.QUser;
import com.bmilab.backend.domain.user.entity.User;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
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
    public Page<GetAllProjectsQueryResult> findAllBySearch(String keyword, Pageable pageable) {
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
                        participant.isLeader.isTrue(),
                        user.name.containsIgnoreCase(keyword)
                ).exists()
                : null;

        List<GetAllProjectsQueryResult> results = queryFactory
                .select(Projections.constructor(
                        GetAllProjectsQueryResult.class,
                        project.id,
                        project.title,
                        project.category,
                        project.startDate,
                        project.endDate,
                        listNullExpression,
                        ExpressionUtils.as(
                                JPAExpressions.select(participant.count())
                                        .from(participant)
                                        .where(participant.project.eq(project)),
                                "participantCount"
                        ),
                        project.fileUrls.size().gt(0)
                ))
                .from(project)
                .where(
                        Expressions.anyOf(titleContains, leaderNameContains)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = Optional.ofNullable(
                queryFactory
                        .select(project.count())
                        .from(project)
                        .where(Expressions.anyOf(titleContains, leaderNameContains))
                        .fetchOne()
        ).orElse(0L);

        List<Long> projectIds = results.stream()
                .map(GetAllProjectsQueryResult::getProjectId)
                .toList();

        List<Tuple> leaderResults = queryFactory.select(participant.project.id, user)
                .from(participant)
                .join(participant.user, user)
                .where(participant.project.id.in(projectIds), participant.isLeader.isTrue())
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
}
