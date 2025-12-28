package com.bmilab.backend.domain.research.paper.repository;

import com.bmilab.backend.domain.research.paper.entity.Journal;
import com.bmilab.backend.domain.research.paper.entity.QJournal;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class JournalRepositoryCustomImpl implements JournalRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Journal> findAllBy(String keyword, Pageable pageable) {
        QJournal journal = QJournal.journal;

        List<Journal> content = queryFactory
                .selectFrom(journal)
                .where(keywordContains(keyword))
                .orderBy(journal.journalName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(journal.count())
                .from(journal)
                .where(keywordContains(keyword));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        QJournal journal = QJournal.journal;
        return journal.journalName.containsIgnoreCase(keyword)
                .or(journal.publisher.containsIgnoreCase(keyword))
                .or(journal.issn.containsIgnoreCase(keyword));
    }
}
