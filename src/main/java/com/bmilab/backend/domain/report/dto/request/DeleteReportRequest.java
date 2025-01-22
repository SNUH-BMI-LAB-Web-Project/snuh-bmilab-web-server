package com.bmilab.backend.domain.report.dto.request;

import java.util.List;

public record DeleteReportRequest(
        List<Long> reportIds
) {
}
