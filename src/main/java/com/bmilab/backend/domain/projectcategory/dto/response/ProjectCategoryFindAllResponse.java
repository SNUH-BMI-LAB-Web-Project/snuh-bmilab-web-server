package com.bmilab.backend.domain.projectcategory.dto.response;

import com.bmilab.backend.domain.projectcategory.entity.ProjectCategory;
import java.util.List;

public record ProjectCategoryFindAllResponse(
        List<ProjectCategorySummary> categories
) {
    public static ProjectCategoryFindAllResponse of(List<ProjectCategory> categories) {
        return new ProjectCategoryFindAllResponse(
                categories.stream()
                        .map(ProjectCategorySummary::from)
                        .toList()
        );
    }
}
