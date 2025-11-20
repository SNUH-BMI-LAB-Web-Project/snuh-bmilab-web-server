package com.bmilab.backend.domain.report.dto.query;

import java.time.LocalDate;

public record UserProjectMissingReportInfo(
        Long userId,
        String userName,
        String userEmail,
        Long projectId,
        String projectTitle,
        LocalDate lastReportDate
) {
}