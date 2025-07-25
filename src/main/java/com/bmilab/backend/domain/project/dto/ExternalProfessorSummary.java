package com.bmilab.backend.domain.project.dto;

import java.util.Objects;

public record ExternalProfessorSummary(
        String name,
        String organization,
        String department,
        String position
) {
    public static ExternalProfessorSummary from(String exProfessorString) {
        if (exProfessorString == null || exProfessorString.equals("///")) {
            return new ExternalProfessorSummary(null, null, null, null);
        }

        String[] split = exProfessorString.split("/", -1);

        if (split.length != 4) {
            throw new IllegalArgumentException("Invalid ex-professor string: " + exProfessorString);
        }

        return new ExternalProfessorSummary(
                split[0],
                split[1],
                split[2],
                (split[3].isBlank() ? null : split[3])
        );
    }

    public boolean equals(ExternalProfessorSummary other) {
        return Objects.equals(name, other.name) &&
                Objects.equals(organization, other.organization) &&
                Objects.equals(department, other.department) &&
                Objects.equals(position, other.position);
    }
}
