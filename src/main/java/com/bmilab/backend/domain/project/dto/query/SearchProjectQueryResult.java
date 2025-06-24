package com.bmilab.backend.domain.project.dto.query;

public record SearchProjectQueryResult(
        Long projectId,
        String title
) {
}
