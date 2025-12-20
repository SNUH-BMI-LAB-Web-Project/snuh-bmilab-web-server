package com.bmilab.backend.domain.research.paper.repository;

import com.bmilab.backend.domain.research.paper.entity.*;
import com.bmilab.backend.domain.research.paper.dto.response.PaperSummaryResponse;
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
public class PaperRepositoryCustomImpl implements PaperRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PaperSummaryResponse> findAllBy(String keyword, Pageable pageable) {
        QPaper paper = QPaper.paper;
        QJournal journal = QJournal.journal;

        BooleanExpression keywordContains = StringUtils.hasText(keyword)
                ? paper.paperTitle.containsIgnoreCase(keyword).or(journal.journalName.containsIgnoreCase(keyword))
                : null;

        List<PaperSummaryResponse> results = queryFactory.select(Projections.constructor(
                        PaperSummaryResponse.class,
                        paper.id,
                        paper.paperTitle,
                        journal.journalName,
                        paper.allAuthors,
                        paper.acceptDate,
                        paper.publishDate
                ))
                .from(paper)
                .leftJoin(paper.journal, journal)
                .where(keywordContains)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(paper.acceptDate.desc()) // Default sort
                .fetch();

        Long count = Optional.ofNullable(queryFactory.select(paper.count())
                .from(paper)
                .leftJoin(paper.journal, journal)
                .where(keywordContains)
                .fetchOne()).orElse(0L);

        return PageableExecutionUtils.getPage(results, pageable, () -> count);
    }
}
