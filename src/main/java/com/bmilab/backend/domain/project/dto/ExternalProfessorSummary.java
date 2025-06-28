package com.bmilab.backend.domain.project.dto;

public record ExternalProfessorSummary(
        String name,
        String organization,
        String department
) {
    public static ExternalProfessorSummary from(String exProfessorString) {
        String[] split = exProfessorString.split("/");

        if (split.length != 3) {
            throw new IllegalArgumentException("Invalid ex-professor string: " + exProfessorString);
        }

        return new ExternalProfessorSummary(
                split[0],
                split[1],
                split[2]
        );
    }
}
