package com.bmilab.backend.domain.research.award.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record CreateAwardRequest(
        @Schema(description = "수상자 목록 (텍스트)", example = "김연구, 이개발")
        String recipients,
        @Schema(description = "연구실 내 수상자 (구성원 연동)")
        List<AwardRecipientRequest> awardRecipients,
        @Schema(description = "수상 날짜", example = "2025-11-15")
        LocalDate awardDate,
        @Schema(description = "주최 기관", example = "대한의료정보학회")
        String hostInstitution,
        @Schema(description = "대회명", example = "2025년 대한의료정보학회 추계학술대회")
        String competitionName,
        @Schema(description = "수상명", example = "우수 포스터상")
        String awardName,
        @Schema(description = "발표 제목", example = "인공지능 기반 의료영상 분석 시스템")
        String presentationTitle,
        @Schema(description = "연계 프로젝트 ID", example = "1")
        Long projectId,
        @Schema(description = "연계 과제 ID (선택적)", example = "1")
        Long taskId
) {
    @Schema(description = "수상자 요청 DTO")
    public record AwardRecipientRequest(
            @Schema(description = "사용자 ID", example = "1")
            Long userId,
            @Schema(description = "역할", example = "대표 수상자")
            String role
    ) {}
}
