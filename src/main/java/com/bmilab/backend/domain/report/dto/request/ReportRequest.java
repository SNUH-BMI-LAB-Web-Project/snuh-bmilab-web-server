package com.bmilab.backend.domain.report.dto.request;

import java.time.LocalDateTime;

public record ReportRequest(
        String tag,
        LocalDateTime dueDate
) {
}