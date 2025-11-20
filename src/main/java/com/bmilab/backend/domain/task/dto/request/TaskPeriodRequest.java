package com.bmilab.backend.domain.task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record TaskPeriodRequest(
        @Schema(description = "연차", example = "1")
        @NotNull(message = "연차는 필수입니다.")
        Integer yearNumber,

        @Schema(description = "시작일", example = "2025-03-01")
        @NotNull(message = "시작일은 필수입니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @Schema(description = "종료일", example = "2026-02-28")
        @NotNull(message = "종료일은 필수입니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate
) {
}