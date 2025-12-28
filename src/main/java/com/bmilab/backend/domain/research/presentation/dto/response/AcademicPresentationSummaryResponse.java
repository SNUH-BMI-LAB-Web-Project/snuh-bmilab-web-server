package com.bmilab.backend.domain.research.presentation.dto.response;

import com.bmilab.backend.domain.research.presentation.entity.AcademicPresentation;
import com.bmilab.backend.domain.research.presentation.entity.AcademicPresentationAuthor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record AcademicPresentationSummaryResponse(
        @Schema(description = "학회 발표 ID")
        Long id,
        @Schema(description = "발표자 목록 (텍스트)")
        String authors,
        @Schema(description = "연구실 내 발표자 목록")
        List<AcademicPresentationResponse.AcademicPresentationAuthorResponse> academicPresentationAuthors,
        @Schema(description = "학회 시작일")
        LocalDate academicPresentationStartDate,
        @Schema(description = "학회 종료일")
        LocalDate academicPresentationEndDate,
        @Schema(description = "학회 장소")
        String academicPresentationLocation,
        @Schema(description = "학회 주최")
        String academicPresentationHost,
        @Schema(description = "학회명")
        String academicPresentationName,
        @Schema(description = "발표 타입")
        String presentationType,
        @Schema(description = "발표 제목")
        String presentationTitle,
        @Schema(description = "연계 프로젝트 ID")
        Long projectId,
        @Schema(description = "연계 프로젝트명")
        String projectName,
        @Schema(description = "연계 과제 ID")
        Long taskId,
        @Schema(description = "연계 과제명")
        String taskName
) {
    public static AcademicPresentationSummaryResponse from(AcademicPresentation ap, List<AcademicPresentationAuthor> authors) {
        return new AcademicPresentationSummaryResponse(
                ap.getId(),
                ap.getAuthors(),
                authors.stream().map(AcademicPresentationResponse.AcademicPresentationAuthorResponse::new).collect(Collectors.toList()),
                ap.getAcademicPresentationStartDate(),
                ap.getAcademicPresentationEndDate(),
                ap.getAcademicPresentationLocation(),
                ap.getAcademicPresentationHost(),
                ap.getAcademicPresentationName(),
                ap.getPresentationType().getDescription(),
                ap.getPresentationTitle(),
                ap.getProject().getId(),
                ap.getProject().getTitle(),
                ap.getTask() != null ? ap.getTask().getId() : null,
                ap.getTask() != null ? ap.getTask().getTitle() : null
        );
    }
}
