package com.bmilab.backend.domain.research.patent.repository;

import com.bmilab.backend.domain.research.patent.entity.*;
import com.bmilab.backend.domain.research.patent.dto.response.PatentSummaryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PatentRepositoryCustomImpl implements PatentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PatentSummaryResponse> findAllBy(String keyword, Pageable pageable) {
        QPatent patent = QPatent.patent;

        BooleanExpression keywordContains = StringUtils.hasText(keyword)
                ? patent.patentName.containsIgnoreCase(keyword).or(patent.applicationNumber.containsIgnoreCase(keyword))
                : null;

        List<PatentSummaryResponse> results = queryFactory.select(Projections.constructor(
                        PatentSummaryResponse.class,
                        patent.id,
                        patent.patentName,
                        patent.applicationNumber,
                        patent.applicantsAll,
                        patent.applicationDate
                ))
                .from(patent)
                .where(keywordContains)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(patent.applicationDate.desc()) // Default sort
                .fetch();

        Long count = Optional.ofNullable(queryFactory.select(patent.count())
                .from(patent)
                .where(keywordContains)
                .fetchOne()).orElse(0L);

        return PageableExecutionUtils.getPage(results, pageable, () -> count);
    }
}
