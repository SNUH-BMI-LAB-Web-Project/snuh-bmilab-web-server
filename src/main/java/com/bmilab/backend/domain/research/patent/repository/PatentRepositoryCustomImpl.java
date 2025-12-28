package com.bmilab.backend.domain.research.patent.repository;

import com.bmilab.backend.domain.project.entity.QProject;
import com.bmilab.backend.domain.research.patent.entity.Patent;
import com.bmilab.backend.domain.research.patent.entity.QPatent;
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
public class PatentRepositoryCustomImpl implements PatentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Patent> findAllBy(String keyword, Pageable pageable) {
        QPatent patent = QPatent.patent;
        QProject project = QProject.project;
        QTask task = QTask.task;

        BooleanExpression keywordContains = StringUtils.hasText(keyword)
                ? patent.patentName.containsIgnoreCase(keyword).or(patent.applicationNumber.containsIgnoreCase(keyword))
                : null;

        List<Patent> results = queryFactory
                .selectFrom(patent)
                .leftJoin(patent.project, project).fetchJoin()
                .leftJoin(patent.task, task).fetchJoin()
                .where(keywordContains)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(patent.applicationDate.desc())
                .fetch();

        Long count = Optional.ofNullable(queryFactory
                .select(patent.count())
                .from(patent)
                .where(keywordContains)
                .fetchOne()).orElse(0L);

        return PageableExecutionUtils.getPage(results, pageable, () -> count);
    }
}
