package com.bmilab.backend.domain.projectcategory.dto.response;

import com.bmilab.backend.domain.projectcategory.entity.ProjectCategory;
import lombok.Builder;

import io.swagger.v3.oas.annotations.media.Schema;

@Builder
public record ProjectCategorySummary(
        @Schema(description = "카테고리 ID", example = "1")
        Long categoryId,

        @Schema(description = "카테고리 이름", example = "NLP")
        String name
) {
    public static ProjectCategorySummary from(ProjectCategory projectCategory) {
        if (projectCategory == null) {
            return null;
        }

        return ProjectCategorySummary.builder()
                .categoryId(projectCategory.getId())
                .name(projectCategory.getName())
                .build();
    }
}