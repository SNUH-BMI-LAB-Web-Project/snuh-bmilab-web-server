package com.bmilab.backend.domain.project.dto.request;

public record ExternalProfessorRequest(
        String name,
        String organization,
        String department
) {
}
