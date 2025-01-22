package com.bmilab.backend.domain.report.dto.response;

import com.bmilab.backend.domain.report.entity.Report;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record ReportDetail(
        Long reportId,
        String tag,
        LocalDateTime dueDate,
        LocalDateTime createdAt,
        List<UserReportDetail> userReports
) {
    public static ReportDetail from(Report report, List<UserReportDetail> userReports) {
        return ReportDetail
                .builder()
                .reportId(report.getId())
                .tag(report.getTag())
                .dueDate(report.getDueDate())
                .createdAt(report.getCreatedAt())
                .userReports(userReports)
                .build();
    }
}
