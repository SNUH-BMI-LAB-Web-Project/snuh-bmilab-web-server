package com.bmilab.backend.domain.task.dto.response;

import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.task.entity.TaskPeriod;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record TaskPeriodResponse(
        @Schema(description = "과제 기간 ID")
        Long id,

        @Schema(description = "년차")
        Integer yearNumber,

        @Schema(description = "시작일", example = "2025-03-01")
        LocalDate startDate,

        @Schema(description = "종료일", example = "2026-02-28")
        LocalDate endDate,

        @Schema(description = "과제 담당자 ID")
        Long managerId,

        @Schema(description = "과제 담당자 이름")
        String managerName,

        @Schema(description = "과제 참여자 목록")
        List<TaskMemberSummary> members,

        @Schema(description = "연차별 관련 파일 목록")
        List<FileSummary> periodFiles,

        @Schema(description = "중간보고 파일 목록")
        List<FileSummary> interimReportFiles,

        @Schema(description = "연차보고 파일 목록")
        List<FileSummary> annualReportFiles
) {
    public static TaskPeriodResponse from(TaskPeriod period) {
        List<TaskMemberSummary> members = period.getMembers().stream()
                .map(user -> new TaskMemberSummary(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());

        return new TaskPeriodResponse(
                period.getId(),
                period.getYearNumber(),
                period.getStartDate(),
                period.getEndDate(),
                period.getManager() != null ? period.getManager().getId() : null,
                period.getManager() != null ? period.getManager().getName() : null,
                members,
                List.of(),
                List.of(),
                List.of()
        );
    }

    public static TaskPeriodResponse from(
            TaskPeriod period,
            List<FileSummary> periodFiles,
            List<FileSummary> interimReportFiles,
            List<FileSummary> annualReportFiles
    ) {
        List<TaskMemberSummary> members = period.getMembers().stream()
                .map(user -> new TaskMemberSummary(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());

        return new TaskPeriodResponse(
                period.getId(),
                period.getYearNumber(),
                period.getStartDate(),
                period.getEndDate(),
                period.getManager() != null ? period.getManager().getId() : null,
                period.getManager() != null ? period.getManager().getName() : null,
                members,
                periodFiles,
                interimReportFiles,
                annualReportFiles
        );
    }
}
