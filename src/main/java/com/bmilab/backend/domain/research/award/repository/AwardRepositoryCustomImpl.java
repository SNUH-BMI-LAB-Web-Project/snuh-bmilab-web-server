package com.bmilab.backend.domain.research.award.repository;

import com.bmilab.backend.domain.project.entity.QProject;
import com.bmilab.backend.domain.research.award.entity.Award;
import com.bmilab.backend.domain.research.award.entity.QAward;
import com.bmilab.backend.domain.task.entity.QTask;
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
    public Page<Award> findAllBy(String keyword, Pageable pageable) {
        QAward award = QAward.award;
        QProject project = QProject.project;
        QTask task = QTask.task;

        BooleanExpression keywordContains = StringUtils.hasText(keyword)
                ? award.awardName.containsIgnoreCase(keyword).or(award.recipients.containsIgnoreCase(keyword))
                : null;

        List<Award> results = queryFactory
                .selectFrom(award)
                .leftJoin(award.project, project).fetchJoin()
                .leftJoin(award.task, task).fetchJoin()
                .where(keywordContains)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(award.awardDate.desc())
                .fetch();

        Long count = Optional.ofNullable(queryFactory
                .select(award.count())
                .from(award)
                .where(keywordContains)
                .fetchOne()).orElse(0L);

        return PageableExecutionUtils.getPage(results, pageable, () -> count);
    }
}
