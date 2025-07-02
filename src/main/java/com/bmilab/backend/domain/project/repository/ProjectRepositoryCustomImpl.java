package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.dto.condition.ProjectFilterCondition;
import com.bmilab.backend.domain.project.dto.query.GetAllProjectsQueryResult;
import com.bmilab.backend.domain.project.dto.query.SearchProjectQueryResult;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.entity.QProject;
import com.bmilab.backend.domain.project.entity.QProjectParticipant;
import com.bmilab.backend.domain.project.enums.ProjectParticipantType;
import com.bmilab.backend.domain.user.entity.QUser;
import com.bmilab.backend.domain.user.entity.User;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SearchProjectQueryResult> searchProject(Long userId, boolean all, String keyword) {

        QProject project = QProject.project;
        QProjectParticipant participant = QProjectParticipant.projectParticipant;

        BooleanExpression participantContains = userId != null ? JPAExpressions.selectOne()
                .from(participant)
                .where(participant.user.id.eq(userId), participant.project.eq(project))
                .exists() : null;

        BooleanExpression userFilter = (all) ? null : project.author.id.eq(userId).or(participantContains);

        BooleanExpression titleContains = (keyword != null) ? project.title.containsIgnoreCase(keyword) : null;
        return queryFactory.select(Projections.constructor(
                        SearchProjectQueryResult.class,
                        project.id.as("projectId"),
                        project.title
                ))
                .from(project)
                .where(userFilter, titleContains)
                .fetch();
    }

    @Override
    public List<Project> findAllByUser(User user) {

        QProject project = QProject.project;
        QProjectParticipant participant = QProjectParticipant.projectParticipant;

        BooleanExpression participantContains = user != null ? JPAExpressions.selectOne()
                .from(participant)
                .where(participant.user.eq(user), participant.project.eq(project))
                .exists() : null;

        return queryFactory.selectFrom(project)
                .where(ExpressionUtils.anyOf(participantContains, project.author.eq(user)))
                .fetch();
    }

    //TODO: 연구 전체 조회 쿼리 리팩터링하기
    @Override
    public Page<GetAllProjectsQueryResult> findAllByFiltering(
            Long userId,
            String keyword,
            ProjectFilterCondition condition,
            Pageable pageable
    ) {

        QProject project = QProject.project;
        QProjectParticipant participant = QProjectParticipant.projectParticipant;
        QUser user = QUser.user;

        Expression<List<User>> listNullExpression = Expressions.constant(Collections.emptyList());

        BooleanExpression titleContains = keyword != null ? project.title.containsIgnoreCase(keyword) : null;

        BooleanExpression piContains = condition.pi() != null ? project.pi.containsIgnoreCase(condition.pi()) : null;

        BooleanExpression practicalProfessorContains = condition.practicalProfessor() != null
                ? project.practicalProfessor.containsIgnoreCase(condition.practicalProfessor())
                : null;

        BooleanExpression leaderNameContains =
                keyword != null ? JPAExpressions.selectOne().from(participant).join(participant.user, user).where(
                        participant.project.eq(project),
                        participant.type.eq(ProjectParticipantType.LEADER),
                        user.name.containsIgnoreCase(keyword)
                ).exists() : null;

        BooleanExpression leaderFilter = condition.leaderId() != null ? JPAExpressions.selectOne()
                .from(participant)
                .join(participant.user, user)
                .where(participant.user.id.eq(condition.leaderId()))
                .exists() : null;

        BooleanExpression statusFilter = condition.status() != null ? project.status.eq(condition.status()) : null;

        BooleanExpression categoryFilter =
                condition.categoryId() != null ? project.category.id.eq(condition.categoryId()) : null;

        BooleanExpression isAccessible = userId != null ? project.author.id.eq(userId)
                .or(JPAExpressions.selectOne()
                        .from(participant)
                        .where(participant.user.id.eq(userId), participant.project.eq(project))
                        .exists()) : null;

        List<GetAllProjectsQueryResult> results = queryFactory.select(Projections.constructor(
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
                                        .where(participant.project.eq(project)), "participantCount"
                        ),
                        project.status,
                        project.isPrivate,
                        isAccessible
                ))
                .from(project)
                .where(
                        ExpressionUtils.anyOf(titleContains, leaderNameContains, piContains, practicalProfessorContains),
                        statusFilter,
                        categoryFilter,
                        leaderFilter
                )
                .orderBy(getCustomSortOrderSpecifier(pageable,project))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = Optional.ofNullable(queryFactory.select(project.count())
                .from(project)
                .where(ExpressionUtils.anyOf(titleContains, leaderNameContains), statusFilter, categoryFilter,
                        leaderFilter, isAccessible)
                .fetchOne()).orElse(0L);

        List<Long> projectIds = results.stream()
                .map(GetAllProjectsQueryResult::getProjectId)
                .toList();

        Map<Long, List<User>> leadersMap;

        if (!projectIds.isEmpty()) {
            List<Tuple> leaderResults = queryFactory.select(participant.project.id, user)
                    .from(participant)
                    .join(participant.user, user)
                    .where(
                            participant.project.id.in(projectIds),
                            participant.type.eq(ProjectParticipantType.LEADER)
                    )
                    .fetch();

            leadersMap = leaderResults.stream()
                    .filter(it -> it.get(0, Long.class) != null && it.get(1, User.class) != null)
                    .collect(Collectors.groupingBy(
                            it -> it.get(0, Long.class),
                            Collectors.mapping(it -> it.get(1, User.class), Collectors.toList())
                    ));
        } else {
            leadersMap = Collections.emptyMap();
        }

        results.forEach(it -> it.setLeaders(leadersMap.getOrDefault(it.getProjectId(), List.of())));

        return PageableExecutionUtils.getPage(results, pageable, () -> count);
    }

    private OrderSpecifier<?>[] getCustomSortOrderSpecifier(Pageable pageable, QProject project) {

        Sort sort = pageable.getSort();

        if (sort.isUnsorted()) {
            return new OrderSpecifier[]{
                    Expressions.numberTemplate(
                            Integer.class,
                            "case when {0} is null then 0 else 1 end",
                            project.endDate
                    ).asc(),
                    project.endDate.desc(),
                    project.startDate.desc()
            };
        }
        for(Sort.Order order : sort) {
            boolean isDesc = order.isDescending();
            String property = order.getProperty();

            switch (property) {
                case "startDate":
                    return new OrderSpecifier[]{
                            isDesc ? project.startDate.desc() : project.startDate.asc()
                    };
                case "endDate":
                    return new OrderSpecifier[]{
                            Expressions.numberTemplate(
                                    Integer.class,
                                    "case when {0} is null then 0 else 1 end",
                                    project.endDate
                            ).asc(), // null 우선
                            isDesc ? project.endDate.desc() : project.endDate.asc()
                    };
                default:
                    return new OrderSpecifier[]{
                            project.endDate.desc()
                    };
            }

        }

        return new OrderSpecifier[]{ project.endDate.desc() };
    }
}
