package com.bmilab.backend.domain.research.presentation.dto.response;

import com.bmilab.backend.domain.research.presentation.entity.AcademicPresentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record AcademicPresentationResponse(
        @Schema(description = "학회 발표 ID", example = "1")
        Long id,
        @Schema(description = "발표자 목록", example = "김연구, 이개발")
        String authors,
        @Schema(description = "학회 시작일", example = "2025-11-10")
        LocalDate conferenceStartDate,
        @Schema(description = "학회 종료일", example = "2025-11-12")
        LocalDate conferenceEndDate,
        @Schema(description = "학회 장소", example = "코엑스")
        String conferenceLocation,
        @Schema(description = "학회 주최", example = "대한의료정보학회")
        String conferenceHost,
        @Schema(description = "학회명", example = "2025년 대한의료정보학회 추계학술대회")
        String conferenceName,
        @Schema(description = "발표 타입", example = "Poster")
        String presentationType,
        @Schema(description = "발표 제목", example = "인공지능 기반 의료영상 분석 시스템")
        String presentationTitle,
        @Schema(description = "연계 프로젝트 ID", example = "1")
        Long projectId,
        @Schema(description = "연계 프로젝트명", example = "의료 AI 솔루션 개발")
        String projectName,
        @Schema(description = "연계 과제 ID", example = "1")
        Long taskId,
        @Schema(description = "연계 과제명", example = "AI 기반 의료영상 분석 솔루션 개발")
        String taskName
) {
    public AcademicPresentationResponse(AcademicPresentation academicPresentation) {
        this(
                academicPresentation.getId(),
                academicPresentation.getAuthors(),
                academicPresentation.getAcademicPresentationStartDate(),
                academicPresentation.getAcademicPresentationEndDate(),
                academicPresentation.getAcademicPresentationLocation(),
                academicPresentation.getAcademicPresentationHost(),
                academicPresentation.getAcademicPresentationName(),
                academicPresentation.getPresentationType().getDescription(),
                academicPresentation.getPresentationTitle(),
                academicPresentation.getProject().getId(),
                academicPresentation.getProject().getTitle(),
                academicPresentation.getTask() != null ? academicPresentation.getTask().getId() : null,
                academicPresentation.getTask() != null ? academicPresentation.getTask().getTitle() : null
        );
    }
}