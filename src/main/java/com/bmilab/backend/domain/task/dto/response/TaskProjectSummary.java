package com.bmilab.backend.domain.task.dto.response;

import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.enums.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record TaskProjectSummary(
        @Schema(description = "프로젝트 ID")
        Long id,

        @Schema(description = "프로젝트 제목")
        String title,

        @Schema(description = "프로젝트 상태")
        ProjectStatus status,

        @Schema(description = "시작일")
        LocalDate startDate,

        @Schema(description = "종료일")
        LocalDate endDate
) {
    public static TaskProjectSummary from(Project project) {
        return new TaskProjectSummary(
                project.getId(),
                project.getTitle(),
                project.getStatus(),
                project.getStartDate(),
                project.getEndDate()
        );
    }
}
