package com.bmilab.backend.domain.project.dto.response;

import com.bmilab.backend.domain.project.entity.ExternalProfessor;
import java.util.List;

public record ExternalProfessorFindAllResponse(
        List<ExternalProfessorItem> externalProfessors
) {
    public static ExternalProfessorFindAllResponse of(List<ExternalProfessor> externalProfessors) {
        return new ExternalProfessorFindAllResponse(
                externalProfessors
                        .stream()
                        .map(ExternalProfessorItem::from)
                        .toList()
        );
    }

    public record ExternalProfessorItem(
            Long professorId,
            String name,
            String organization,
            String department
    ) {
        public static ExternalProfessorItem from(ExternalProfessor externalProfessor) {
            return new ExternalProfessorItem(
                    externalProfessor.getId(),
                    externalProfessor.getName(),
                    externalProfessor.getOrganization(),
                    externalProfessor.getDepartment()
            );
        }
    }
}
