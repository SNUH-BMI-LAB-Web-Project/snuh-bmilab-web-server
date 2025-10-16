package com.bmilab.backend.domain.task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TaskPeriodUpdateRequest(
        @Schema(description = "시작일", example = "2025-03-01")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @Schema(description = "종료일", example = "2026-02-28")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate,

        @Schema(description = "과제 담당자 ID", example = "1")
        Long managerId,

        @Schema(description = "과제 참여자 ID 목록")
        List<Long> memberIds,

        @Schema(description = "연차별 관련 파일 ID 목록")
        List<UUID> periodFileIds,

        @Schema(description = "중간보고 파일 ID 목록")
        List<UUID> interimReportFileIds,

        @Schema(description = "연차보고 파일 ID 목록")
        List<UUID> annualReportFileIds
) {
}
