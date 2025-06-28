package com.bmilab.backend.domain.project.dto.condition;

import com.bmilab.backend.domain.project.enums.ProjectStatus;

public record ProjectFilterCondition(
        Long leaderId,
        Long categoryId,
        ProjectStatus status,
        String pi,
        String practicalProfessor
) {
    public static ProjectFilterCondition of(Long leaderId, Long categoryId, ProjectStatus status, String pi, String practicalProfessor) {
        return new ProjectFilterCondition(leaderId, categoryId, status, pi, practicalProfessor);
    }
}
