package com.bmilab.backend.domain.research.patent.dto.response;

import com.bmilab.backend.domain.file.dto.response.FileSummary;
import com.bmilab.backend.domain.research.patent.entity.Patent;
import com.bmilab.backend.domain.research.patent.entity.PatentAuthor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record PatentSummaryResponse(
        @Schema(description = "특허 ID")
        Long id,
        @Schema(description = "출원일자")
        LocalDate applicationDate,
        @Schema(description = "출원번호")
        String applicationNumber,
        @Schema(description = "출원명")
        String patentName,
        @Schema(description = "출원인(전체)")
        String applicantsAll,
        @Schema(description = "출원인(연구실)")
        List<PatentResponse.PatentAuthorResponse> patentAuthors,
        @Schema(description = "비고")
        String remarks,
        @Schema(description = "연계 프로젝트 ID")
        Long projectId,
        @Schema(description = "연계 프로젝트명")
        String projectName,
        @Schema(description = "연계 과제 ID")
        Long taskId,
        @Schema(description = "연계 과제명")
        String taskName,
        @Schema(description = "첨부 파일 목록")
        List<FileSummary> files
) {
    public static PatentSummaryResponse from(Patent patent, List<PatentAuthor> patentAuthors, List<FileSummary> files) {
        return new PatentSummaryResponse(
                patent.getId(),
                patent.getApplicationDate(),
                patent.getApplicationNumber(),
                patent.getPatentName(),
                patent.getApplicantsAll(),
                patentAuthors.stream().map(PatentResponse.PatentAuthorResponse::new).collect(Collectors.toList()),
                patent.getRemarks(),
                patent.getProject().getId(),
                patent.getProject().getTitle(),
                patent.getTask() != null ? patent.getTask().getId() : null,
                patent.getTask() != null ? patent.getTask().getTitle() : null,
                files
        );
    }
}
