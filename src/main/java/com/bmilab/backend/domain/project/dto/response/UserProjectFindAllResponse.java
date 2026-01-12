package com.bmilab.backend.domain.project.dto.response;

import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.enums.ProjectStatus;
import com.bmilab.backend.domain.projectcategory.dto.response.ProjectCategorySummary;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

public record UserProjectFindAllResponse(
        List<UserProjectItem> projects
) {
        public static UserProjectFindAllResponse of(List<Project> projects) {
                return new UserProjectFindAllResponse(
                        projects
                                .stream()
                                .map(UserProjectItem::from)
                                .toList()
                );
        }

        @Builder
        public record UserProjectItem(
                @Schema(description = "연구 ID", example = "42")
                Long projectId,

                @Schema(description = "연구 제목", example = "AI 콘텐츠 자동 생성 시스템")
                String title,

                @Schema(description = "연구 상태", example = "IN_PROGRESS")
                ProjectStatus status,

                @Schema(description = "연구 분야")
                ProjectCategorySummary category,

                @Schema(description = "연구 시작일", example = "2025-05-01")
                LocalDate startDate,

                @Schema(description = "연구 종료일", example = "2025-06-30")
                LocalDate endDate
        ) {
                public static UserProjectItem from(Project project) {
                        return UserProjectItem
                                .builder()
                                .projectId(project.getId())
                                .title(project.getTitle())
                                .status(project.getEffectiveStatus())
                                .category(project.getCategory() == null ? null : ProjectCategorySummary.from(project.getCategory()))
                                .startDate(project.getStartDate())
                                .endDate(project.getEndDate())
                                .build();
                }
        }
}
