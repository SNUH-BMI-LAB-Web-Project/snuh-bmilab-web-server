package com.bmilab.backend.domain.project.dto.condition;

import com.bmilab.backend.domain.project.enums.ProjectCategory;
import com.bmilab.backend.domain.project.enums.ProjectStatus;

public record ProjectFilterCondition(
        Long leaderId,
        ProjectCategory category,
        ProjectStatus status,
        String pi,
        String practicalProfessor
) {
    public static ProjectFilterCondition of(Long leaderId, ProjectCategory category, ProjectStatus status, String pi, String practicalProfessor) {
        return new ProjectFilterCondition(leaderId, category, status, pi, practicalProfessor);
    }
}
