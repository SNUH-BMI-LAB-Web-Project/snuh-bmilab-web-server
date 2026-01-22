package com.bmilab.backend.domain.research.paper.repository;

import com.bmilab.backend.domain.research.paper.entity.Paper;
import com.bmilab.backend.domain.research.paper.entity.QJournal;
import com.bmilab.backend.domain.research.paper.entity.QPaper;
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
    public Page<Paper> findAllBy(String keyword, Pageable pageable) {
        QPaper paper = QPaper.paper;
        QJournal journal = QJournal.journal;

        BooleanExpression keywordContains = StringUtils.hasText(keyword)
                ? paper.paperTitle.containsIgnoreCase(keyword)
                        .or(journal.journalName.containsIgnoreCase(keyword))
                        .or(paper.allAuthors.containsIgnoreCase(keyword))
                : null;

        List<Paper> results = queryFactory
                .selectFrom(paper)
                .leftJoin(paper.journal, journal).fetchJoin()
                .where(keywordContains)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(paper.acceptDate.desc())
                .fetch();

        Long count = Optional.ofNullable(queryFactory
                .select(paper.count())
                .from(paper)
                .leftJoin(paper.journal, journal)
                .where(keywordContains)
                .fetchOne()).orElse(0L);

        return PageableExecutionUtils.getPage(results, pageable, () -> count);
    }
}
