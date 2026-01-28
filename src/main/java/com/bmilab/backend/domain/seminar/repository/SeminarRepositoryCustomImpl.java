package com.bmilab.backend.domain.seminar.repository;

import com.bmilab.backend.domain.seminar.entity.QSeminar;
import com.bmilab.backend.domain.seminar.entity.Seminar;
import com.bmilab.backend.domain.seminar.enums.SeminarLabel;
import com.bmilab.backend.domain.user.entity.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class SeminarRepositoryCustomImpl implements SeminarRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Seminar> findAllByDateRange(LocalDate startDate, LocalDate endDate) {
        QSeminar seminar = QSeminar.seminar;
        QUser user = QUser.user;

        return queryFactory
                .selectFrom(seminar)
                .leftJoin(seminar.user, user).fetchJoin()
                .where(dateRangeCondition(seminar, startDate, endDate))
                .orderBy(seminar.startDate.asc())
                .fetch();
    }

    @Override
    public Page<Seminar> searchSeminars(String keyword, SeminarLabel label, Pageable pageable) {
        QSeminar seminar = QSeminar.seminar;
        QUser user = QUser.user;

        List<Seminar> content = queryFactory
                .selectFrom(seminar)
                .leftJoin(seminar.user, user).fetchJoin()
                .where(
                        keywordCondition(seminar, keyword),
                        labelCondition(seminar, label)
                )
                .orderBy(seminar.startDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(seminar.count())
                .from(seminar)
                .where(
                        keywordCondition(seminar, keyword),
                        labelCondition(seminar, label)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression dateRangeCondition(QSeminar seminar, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        // 세미나 기간이 조회 기간과 겹치는 경우 조회
        // (seminar.startDate <= endDate) AND (seminar.endDate >= startDate OR (seminar.endDate IS NULL AND seminar.startDate >= startDate))
        return seminar.startDate.loe(endDate)
                .and(
                        seminar.endDate.goe(startDate)
                                .or(seminar.endDate.isNull().and(seminar.startDate.goe(startDate)))
                );
    }

    private BooleanExpression keywordCondition(QSeminar seminar, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return seminar.title.containsIgnoreCase(keyword)
                .or(seminar.note.containsIgnoreCase(keyword));
    }

    private BooleanExpression labelCondition(QSeminar seminar, SeminarLabel label) {
        return label != null ? seminar.label.eq(label) : null;
    }
}
