package com.bmilab.backend.domain.research.award.dto.response;

import com.bmilab.backend.domain.research.award.entity.Award;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record AwardResponse(
        @Schema(description = "수상 ID", example = "1")
        Long id,
        @Schema(description = "수상자 목록", example = "김연구, 이개발")
        String recipients,
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
        @Schema(description = "연계 프로젝트명", example = "의료 AI 솔루션 개발")
        String projectName
) {
    public AwardResponse(Award award) {
        this(
                award.getId(),
                award.getRecipients(),
                award.getAwardDate(),
                award.getHostInstitution(),
                award.getCompetitionName(),
                award.getAwardName(),
                award.getPresentationTitle(),
                award.getProject().getId(),
                award.getProject().getTitle()
        );
    }
}
