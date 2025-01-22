package com.bmilab.backend.domain.report.dto.response;

import com.bmilab.backend.domain.report.entity.Report;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ReportSummary(
        Long reportId,
        String tag,
        LocalDateTime dueDate,
        LocalDateTime createdAt
) {
    public static ReportSummary from(Report report) {
        return ReportSummary
                .builder()
                .reportId(report.getId())
                .tag(report.getTag())
                .dueDate(report.getDueDate())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
