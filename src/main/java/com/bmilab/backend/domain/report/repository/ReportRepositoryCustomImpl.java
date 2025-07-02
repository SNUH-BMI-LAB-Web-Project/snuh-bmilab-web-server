package com.bmilab.backend.domain.report.repository;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.file.entity.QFileInformation;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.report.dto.query.GetAllReportsQueryResult;
import com.bmilab.backend.domain.report.entity.QReport;
import com.bmilab.backend.domain.report.entity.Report;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ReportRepositoryCustomImpl implements ReportRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<GetAllReportsQueryResult> findAllWithFiles(
            Long userId,
            Long projectId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword
    ) {

        QFileInformation file = QFileInformation.fileInformation;
        QReport report = QReport.report;

        BooleanExpression projectFilter = projectId != null ? report.project.id.eq(projectId) : null;

        BooleanExpression userFilter = userId != null ? report.user.id.eq(userId) : null;

        BooleanExpression dateBetween = dateBetween(startDate, endDate, report);

        BooleanExpression keywordFilter = (keyword != null && !keyword.isBlank())
                ? report.content.containsIgnoreCase(keyword)
                .or(report.project.title.containsIgnoreCase(keyword))
                .or(report.user.name.containsIgnoreCase(keyword))
                : null;

        List<Report> reports = queryFactory.selectFrom(report)
                .where(ExpressionUtils.allOf(userFilter, projectFilter, dateBetween))
                .fetch();

        Map<Long, List<FileInformation>> fileMap = queryFactory.selectFrom(file)
                .where(
                        file.domainType.eq(FileDomainType.REPORT),
                        file.entityId.in(reports.stream().map(Report::getId).toList())
                )
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(FileInformation::getEntityId));

        return reports.stream()
                .map(r -> new GetAllReportsQueryResult(r, fileMap.getOrDefault(r.getId(), List.of())))
                .toList();
    }

    public BooleanExpression dateBetween(LocalDate startDate, LocalDate endDate, QReport report) {

        if (startDate != null && endDate != null) {
            return report.date.between(startDate, endDate);
        } else if (startDate != null) {
            return report.date.goe(startDate);
        } else if (endDate != null) {
            return report.date.loe(endDate);
        } else {
            return null;
        }
    }
}
