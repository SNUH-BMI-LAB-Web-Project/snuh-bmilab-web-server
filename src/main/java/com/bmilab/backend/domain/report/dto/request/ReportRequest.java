package com.bmilab.backend.domain.report.dto.request;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ReportRequest(
        Long projectId,
        LocalDate date,
        String content,
        List<UUID> fileIds
) {
}
