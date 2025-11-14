package com.bmilab.backend.domain.report.repository;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.file.entity.QFileInformation;
import com.bmilab.backend.domain.file.enums.FileDomainType;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.entity.ProjectParticipant;
import com.bmilab.backend.domain.project.entity.QProject;
import com.bmilab.backend.domain.project.entity.QProjectParticipant;
import com.bmilab.backend.domain.project.enums.ProjectParticipantType;
import com.bmilab.backend.domain.report.dto.query.GetAllReportsQueryResult;
import com.bmilab.backend.domain.report.dto.query.ProjectMissingReportInfo;
import com.bmilab.backend.domain.report.dto.query.UserProjectMissingReportInfo;
import com.bmilab.backend.domain.report.entity.QReport;
import com.bmilab.backend.domain.report.entity.Report;
import com.bmilab.backend.domain.user.entity.QUser;
import com.bmilab.backend.domain.user.entity.User;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ReportRepositoryCustomImpl implements ReportRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<GetAllReportsQueryResult> findReportsByUser(Long userId, Long projectId, LocalDate startDate, LocalDate endDate) {
        QFileInformation file = QFileInformation.fileInformation;
        QReport report = QReport.report;

        BooleanExpression projectFilter = projectId != null ? report.project.id.eq(projectId) : null;

        BooleanExpression userFilter = userId != null ? report.user.id.eq(userId) : null;

        BooleanExpression dateBetween = dateBetween(startDate, endDate, report);

        List<Report> reports = queryFactory
                .selectFrom(report)
                .where(ExpressionUtils.allOf(
                        userFilter,
                        projectFilter,
                        dateBetween
                ))
                .orderBy(report.date.desc())
                .fetch();

        Map<Long, List<FileInformation>> fileMap = queryFactory
                .selectFrom(file)
                .where(
                        file.domainType.eq(FileDomainType.REPORT),
                        file.entityId.in(reports.stream().map(Report::getId).toList())
                )
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(FileInformation::getEntityId));

        return reports.stream()
                .map(r -> new GetAllReportsQueryResult(
                        r,
                        fileMap.getOrDefault(r.getId(), List.of())
                ))
                .toList();
    }

    public List<GetAllReportsQueryResult> findAllByUser(Long userId) {
        QFileInformation file = QFileInformation.fileInformation;
        QReport report = QReport.report;

        BooleanExpression userFilter = userId != null ? report.user.id.eq(userId) : null;

        List<Report> reports = queryFactory
                .selectFrom(report)
                .where(ExpressionUtils.allOf(userFilter))
                .fetch();

        Map<Long, List<FileInformation>> fileMap = queryFactory
                .selectFrom(file)
                .where(
                        file.domainType.eq(FileDomainType.REPORT),
                        file.entityId.in(reports.stream().map(Report::getId).toList())
                )
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(FileInformation::getEntityId));

        return reports.stream()
                .map(r -> new GetAllReportsQueryResult(
                        r,
                        fileMap.getOrDefault(r.getId(), List.of())
                ))
                .toList();
    }

    public List<GetAllReportsQueryResult> findAllByDateWithFiles(LocalDate date) {
        QFileInformation file = QFileInformation.fileInformation;
        QReport report = QReport.report;

        List<Report> reports = queryFactory
                .selectFrom(report)
                .where(report.date.eq(date))
                .fetch();

        Map<Long, List<FileInformation>> fileMap = queryFactory
                .selectFrom(file)
                .where(
                        file.domainType.eq(FileDomainType.REPORT),
                        file.entityId.in(reports.stream().map(Report::getId).toList())
                )
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(FileInformation::getEntityId));

        return reports.stream()
                .map(r -> new GetAllReportsQueryResult(
                        r,
                        fileMap.getOrDefault(r.getId(), List.of())
                ))
                .toList();
    }

    public List<GetAllReportsQueryResult> findReportsByCondition(
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
                .where(ExpressionUtils.allOf(userFilter, projectFilter, dateBetween, keywordFilter))
                .orderBy(report.date.desc())
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

    @Override
    public List<ProjectMissingReportInfo> findProjectsMissingReportsInPeriod(LocalDate startDate, LocalDate endDate) {
        QReport report = QReport.report;
        QProject project = QProject.project;
        QProjectParticipant participant = QProjectParticipant.projectParticipant;

        // 2주 내 보고가 있는 프로젝트 ID 목록
        List<Long> projectIdsWithReports = queryFactory
                .select(report.project.id)
                .from(report)
                .where(
                        report.project.id.isNotNull(),
                        report.date.between(startDate, endDate)
                )
                .distinct()
                .fetch();

        // 전체 프로젝트 중 2주 내 보고가 없는 프로젝트 조회
        List<Project> projectsWithoutReports = queryFactory
                .selectFrom(project)
                .where(project.id.notIn(projectIdsWithReports))
                .fetch();

        // 각 프로젝트의 마지막 보고 날짜 및 첫 번째 리더 조회
        return projectsWithoutReports.stream()
                .map(p -> {
                    QReport r = QReport.report;
                    LocalDate lastReportDate = queryFactory
                            .select(r.date.max())
                            .from(r)
                            .where(r.project.id.eq(p.getId()))
                            .fetchOne();

                    // 첫 번째 리더 이름 조회
                    QProjectParticipant pp = QProjectParticipant.projectParticipant;
                    QUser user = QUser.user;
                    List<ProjectParticipant> leaders = queryFactory
                            .selectFrom(pp)
                            .join(pp.user, user).fetchJoin()
                            .where(
                                    pp.project.id.eq(p.getId()),
                                    pp.type.eq(ProjectParticipantType.LEADER),
                                    pp.leftAt.isNull()
                            )
                            .fetch();

                    String firstLeaderName = null;
                    if (!leaders.isEmpty()) {
                        firstLeaderName = leaders.get(0).getUser().getName();
                    }

                    return new ProjectMissingReportInfo(
                            p.getId(),
                            p.getTitle(),
                            firstLeaderName,
                            lastReportDate
                    );
                })
                .toList();
    }

    @Override
    public List<UserProjectMissingReportInfo> findUserProjectsMissingReportsInPeriod(LocalDate startDate, LocalDate endDate) {
        QReport report = QReport.report;
        QUser user = QUser.user;
        QProject project = QProject.project;

        // 모든 유저-프로젝트 조합 조회 (과거에 보고한 적이 있는 조합)
        List<Report> allUserProjectReports = queryFactory
                .selectFrom(report)
                .where(report.project.id.isNotNull())
                .fetch();

        // 유저-프로젝트 조합별로 그룹핑
        Map<String, Report> userProjectMap = new HashMap<>();
        for (Report r : allUserProjectReports) {
            String key = r.getUser().getId() + "_" + r.getProject().getId();
            userProjectMap.putIfAbsent(key, r);
        }

        // 2주 내 보고가 있는 유저-프로젝트 조합
        List<Report> recentReports = queryFactory
                .selectFrom(report)
                .where(
                        report.project.id.isNotNull(),
                        report.date.between(startDate, endDate)
                )
                .fetch();

        Map<String, Boolean> hasRecentReport = new HashMap<>();
        for (Report r : recentReports) {
            String key = r.getUser().getId() + "_" + r.getProject().getId();
            hasRecentReport.put(key, true);
        }

        // 2주 동안 보고하지 않은 유저-프로젝트 조합 필터링
        return userProjectMap.entrySet().stream()
                .filter(entry -> !hasRecentReport.containsKey(entry.getKey()))
                .map(entry -> {
                    Report r = entry.getValue();
                    User u = r.getUser();
                    Project p = r.getProject();

                    // 해당 유저-프로젝트 조합의 마지막 보고 날짜 조회
                    QReport reportAlias = QReport.report;
                    LocalDate lastReportDate = queryFactory
                            .select(reportAlias.date.max())
                            .from(reportAlias)
                            .where(
                                    reportAlias.user.id.eq(u.getId()),
                                    reportAlias.project.id.eq(p.getId())
                            )
                            .fetchOne();

                    return new UserProjectMissingReportInfo(
                            u.getId(),
                            u.getName(),
                            u.getEmail(),
                            p.getId(),
                            p.getTitle(),
                            lastReportDate
                    );
                })
                .toList();
    }
}
