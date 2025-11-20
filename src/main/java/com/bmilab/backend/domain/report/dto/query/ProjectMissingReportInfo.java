package com.bmilab.backend.domain.report.dto.query;

import java.time.LocalDate;

public record ProjectMissingReportInfo(
        Long projectId,
        String projectTitle,
        String firstLeaderName,
        LocalDate lastReportDate
) {
}