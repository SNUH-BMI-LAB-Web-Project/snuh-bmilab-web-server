package com.bmilab.backend.domain.project.dto.condition;

import com.bmilab.backend.domain.project.enums.ProjectCategory;
import com.bmilab.backend.domain.project.enums.ProjectStatus;

public record ProjectFilterCondition(
        Long leaderId,
        ProjectCategory category,
        ProjectStatus status
) {
    public static ProjectFilterCondition of(Long leaderId, ProjectCategory category, ProjectStatus status) {
        return new ProjectFilterCondition(leaderId, category, status);
    }
}
