package com.bmilab.backend.domain.project.dto;

public record ExternalProfessorSummary(
        String name,
        String organization,
        String department,
        String affiliation
) {
    public static ExternalProfessorSummary from(String exProfessorString) {
        String[] split = exProfessorString.split("/");

        if (split.length != 4) {
            throw new IllegalArgumentException("Invalid ex-professor string: " + exProfessorString);
        }

        return new ExternalProfessorSummary(
                split[0],
                split[1],
                split[2],
                split[3]
        );
    }
}
