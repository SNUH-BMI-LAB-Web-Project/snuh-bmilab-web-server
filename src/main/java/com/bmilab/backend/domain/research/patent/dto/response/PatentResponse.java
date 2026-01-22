package com.bmilab.backend.domain.research.patent.dto.response;

import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.research.patent.entity.Patent;
import com.bmilab.backend.domain.research.patent.entity.PatentAuthor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record PatentResponse(
        Long id,
        LocalDate applicationDate,
        String applicationNumber,
        String patentName,
        String applicantsAll,
        List<PatentAuthorResponse> patentAuthors,
        String remarks,
        Long projectId,
        String projectName,
        Long taskId,
        String taskName,
        List<FileSummary> files
) {
    public PatentResponse(Patent patent, List<PatentAuthor> patentAuthors, List<FileSummary> files) {
        this(
                patent.getId(),
                patent.getApplicationDate(),
                patent.getApplicationNumber(),
                patent.getPatentName(),
                patent.getApplicantsAll(),
                patentAuthors.stream().map(PatentAuthorResponse::new).collect(Collectors.toList()),
                patent.getRemarks(),
                patent.getProject().getId(),
                patent.getProject().getTitle(),
                patent.getTask() != null ? patent.getTask().getId() : null,
                patent.getTask() != null ? patent.getTask().getTitle() : null,
                files
        );
    }

    public record PatentAuthorResponse(
            Long userId,
            String userName,
            String role
    ) {
        public PatentAuthorResponse(PatentAuthor patentAuthor) {
            this(
                    patentAuthor.getUser().getId(),
                    patentAuthor.getUser().getName(),
                    patentAuthor.getRole()
            );
        }
    }
}
