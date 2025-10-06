package com.bmilab.backend.domain.task.dto.response;

import com.bmilab.backend.domain.task.entity.TaskPeriod;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

public record TaskPeriodResponse(
        @Schema(description = "과제 기간 ID")
        Long id,

        @Schema(description = "년차")
        Integer yearNumber,

        @Schema(description = "과제 담당자 ID")
        Long managerId,

        @Schema(description = "과제 담당자 이름")
        String managerName,

        @Schema(description = "과제 참여자 목록")
        List<TaskMemberSummary> members
) {
    public static TaskPeriodResponse from(TaskPeriod period) {
        List<TaskMemberSummary> members = period.getMembers().stream()
                .map(user -> new TaskMemberSummary(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());

        return new TaskPeriodResponse(
                period.getId(),
                period.getYearNumber(),
                period.getManager() != null ? period.getManager().getId() : null,
                period.getManager() != null ? period.getManager().getName() : null,
                members
        );
    }
}
