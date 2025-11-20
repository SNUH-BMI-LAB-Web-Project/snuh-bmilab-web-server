package com.bmilab.backend.domain.task.dto.response;

import com.bmilab.backend.domain.task.entity.TaskPeriod;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record TaskPeriodSummaryResponse(
        @Schema(description = "과제 기간 ID")
        Long id,

        @Schema(description = "년차")
        Integer yearNumber,

        @Schema(description = "시작일", example = "2025-03-01")
        LocalDate startDate,

        @Schema(description = "종료일", example = "2026-02-28")
        LocalDate endDate
) {
    public static TaskPeriodSummaryResponse from(TaskPeriod period) {
        return new TaskPeriodSummaryResponse(
                period.getId(),
                period.getYearNumber(),
                period.getStartDate(),
                period.getEndDate()
        );
    }
}
