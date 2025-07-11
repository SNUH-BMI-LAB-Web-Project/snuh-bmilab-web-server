package com.bmilab.backend.domain.project.dto;

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
}
