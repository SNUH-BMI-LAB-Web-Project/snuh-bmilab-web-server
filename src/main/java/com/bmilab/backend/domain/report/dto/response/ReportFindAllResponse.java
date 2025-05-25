package com.bmilab.backend.domain.report.dto.response;

import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.report.dto.query.GetAllReportsQueryResult;
import com.bmilab.backend.domain.report.entity.Report;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public record ReportFindAllResponse(
        List<ReportSummary> reports
) {
    public static ReportFindAllResponse of(List<GetAllReportsQueryResult> queryResults) {
        return new ReportFindAllResponse(
                queryResults.stream()
                        .map(result ->
                                ReportSummary.from(result.report(), result.files()))
                        .toList()
        );
    }

    @Builder
    public record ReportSummary(
            Long reportId,
            UserSummary user,
            ReportProjectSummary project,
            LocalDate date,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            List<FileSummary> files
    ) {
        @Builder
        public static ReportSummary from(Report report, List<FileInformation> files) {
            return ReportSummary.builder()
                    .reportId(report.getId())
                    .user(UserSummary.from(report.getUser()))
                    .project(ReportProjectSummary.from(report.getProject()))
                    .date(report.getDate())
                    .content(report.getContent())
                    .createdAt(report.getCreatedAt())
                    .updatedAt(report.getUpdatedAt())
                    .files(files.stream().map(FileSummary::from).toList())
                    .build();
        }
    }
}
