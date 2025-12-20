package com.bmilab.backend.domain.research.presentation.repository;

import com.bmilab.backend.domain.research.presentation.entity.*;
import com.bmilab.backend.domain.research.presentation.dto.response.AcademicPresentationSummaryResponse;
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
public class AcademicPresentationRepositoryCustomImpl implements AcademicPresentationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AcademicPresentationSummaryResponse> findAllBy(String keyword, Pageable pageable) {
        QAcademicPresentation academicPresentation = QAcademicPresentation.academicPresentation;

        BooleanExpression keywordContains = StringUtils.hasText(keyword)
                ? academicPresentation.academicPresentationName.containsIgnoreCase(keyword).or(academicPresentation.presentationTitle.containsIgnoreCase(keyword))
                : null;

        List<AcademicPresentationSummaryResponse> results = queryFactory.select(Projections.constructor(
                        AcademicPresentationSummaryResponse.class,
                        academicPresentation.id,
                        academicPresentation.academicPresentationName,
                        academicPresentation.presentationTitle,
                        academicPresentation.authors,
                        academicPresentation.academicPresentationStartDate,
                        academicPresentation.academicPresentationEndDate
                ))
                .from(academicPresentation)
                .where(keywordContains)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(academicPresentation.academicPresentationStartDate.desc())
                .fetch();

        Long count = Optional.ofNullable(queryFactory.select(academicPresentation.count())
                .from(academicPresentation)
                .where(keywordContains)
                .fetchOne()).orElse(0L);

        return PageableExecutionUtils.getPage(results, pageable, () -> count);
    }
}