package com.bmilab.backend.domain.research.award.repository;

import com.bmilab.backend.domain.research.award.entity.*;
import com.bmilab.backend.domain.research.award.dto.response.AwardSummaryResponse;
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
public class AwardRepositoryCustomImpl implements AwardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AwardSummaryResponse> findAllBy(String keyword, Pageable pageable) {
        QAward award = QAward.award;

        BooleanExpression keywordContains = StringUtils.hasText(keyword)
                ? award.awardName.containsIgnoreCase(keyword).or(award.recipients.containsIgnoreCase(keyword))
                : null;

        List<AwardSummaryResponse> results = queryFactory.select(Projections.constructor(
                        AwardSummaryResponse.class,
                        award.id,
                        award.awardName,
                        award.recipients,
                        award.competitionName,
                        award.awardDate
                ))
                .from(award)
                .where(keywordContains)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(award.awardDate.desc()) // Default sort
                .fetch();

        Long count = Optional.ofNullable(queryFactory.select(award.count())
                .from(award)
                .where(keywordContains)
                .fetchOne()).orElse(0L);

        return PageableExecutionUtils.getPage(results, pageable, () -> count);
    }
}
