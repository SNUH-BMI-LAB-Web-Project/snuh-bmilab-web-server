package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.dto.ExternalProfessorSummary;
import com.bmilab.backend.domain.project.entity.QExternalProfessor;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExternalProfessorRepositoryCustomImpl implements ExternalProfessorRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ExternalProfessorSummary> findExists(Iterable<ExternalProfessorSummary> keys) {
        QExternalProfessor ep = QExternalProfessor.externalProfessor;

        BooleanBuilder conditionBuilder = new BooleanBuilder();

        keys.forEach((key) ->
                conditionBuilder.or(
                        ep.name.eq(key.name())
                                .and(ep.organization.eq(key.organization()))
                                .and(ep.department.eq(key.department()))
                                .and(ep.affiliation.eq(key.affiliation()))
                )
        );

        return queryFactory
                .select(Projections.constructor(
                        ExternalProfessorSummary.class,
                        ep.name,
                        ep.organization,
                        ep.department,
                        ep.affiliation
                ))
                .from(ep)
                .where(conditionBuilder)
                .fetch();
    }
}
