package com.bmilab.backend.domain.research.publication.repository;

import com.bmilab.backend.domain.research.publication.entity.*;
import com.bmilab.backend.domain.research.publication.dto.response.AuthorSummaryResponse;
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
public class AuthorRepositoryCustomImpl implements AuthorRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AuthorSummaryResponse> findAllBy(String keyword, Pageable pageable) {
        QAuthor author = QAuthor.author;

        BooleanExpression keywordContains = StringUtils.hasText(keyword)
                ? author.publicationName.containsIgnoreCase(keyword).or(author.title.containsIgnoreCase(keyword))
                : null;

        List<AuthorSummaryResponse> results = queryFactory.select(Projections.constructor(
                        AuthorSummaryResponse.class,
                        author.id,
                        author.publicationName,
                        author.title,
                        author.authors,
                        author.publicationDate
                ))
                .from(author)
                .where(keywordContains)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(author.publicationDate.desc())
                .fetch();

        Long count = Optional.ofNullable(queryFactory.select(author.count())
                .from(author)
                .where(keywordContains)
                .fetchOne()).orElse(0L);

        return PageableExecutionUtils.getPage(results, pageable, () -> count);
    }
}
