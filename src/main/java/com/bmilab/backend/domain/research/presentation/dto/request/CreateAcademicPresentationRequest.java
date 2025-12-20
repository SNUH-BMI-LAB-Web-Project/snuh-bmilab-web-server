package com.bmilab.backend.domain.research.presentation.dto.request;

import com.bmilab.backend.domain.research.presentation.enums.AcademicPresentationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record CreateAcademicPresentationRequest(
        @Schema(description = "발표자 목록 (텍스트)", example = "김연구, 이개발")
        String authors,
        @Schema(description = "연구실 내 발표자 (구성원 연동)")
        List<AcademicPresentationAuthorRequest> academicPresentationAuthors,
        @Schema(description = "학회 시작일", example = "2025-11-10")
        LocalDate academicPresentationStartDate,
        @Schema(description = "학회 종료일", example = "2025-11-12")
        LocalDate academicPresentationEndDate,
        @Schema(description = "학회 장소", example = "코엑스")
        String academicPresentationLocation,
        @Schema(description = "학회 주최", example = "대한의료정보학회")
        String academicPresentationHost,
        @Schema(description = "학회명", example = "2025년 대한의료정보학회 추계학술대회")
        String academicPresentationName,
        @Schema(description = "발표 타입", example = "POSTER")
        AcademicPresentationType presentationType,
        @Schema(description = "발표 제목", example = "인공지능 기반 의료영상 분석 시스템")
        String presentationTitle,
        @Schema(description = "연계 프로젝트 ID", example = "1")
        Long projectId,
        @Schema(description = "연계 과제 ID", example = "1")
        Long taskId
) {
    @Schema(description = "학회 발표자 요청 DTO")
    public record AcademicPresentationAuthorRequest(
            @Schema(description = "사용자 ID", example = "1")
            Long userId,
            @Schema(description = "역할", example = "발표자")
            String role
    ) {}
}