package com.bmilab.backend.domain.report.dto.response;

import java.util.List;

public record ReportFindAllResponse(
        List<ReportSummary> reports
) {
    public static ReportFindAllResponse of(List<ReportSummary> reports) {
        return new ReportFindAllResponse(reports);
    }
}
