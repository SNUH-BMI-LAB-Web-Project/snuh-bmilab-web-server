package com.bmilab.backend.domain.research.award.dto.response;

import com.bmilab.backend.domain.research.award.entity.Award;
import com.bmilab.backend.domain.research.award.entity.AwardRecipient;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record AwardResponse(
        @Schema(description = "수상 ID", example = "1")
        Long id,
        @Schema(description = "수상자 목록 (텍스트)", example = "김연구, 이개발")
        String recipients,
        @Schema(description = "연구실 내 수상자 목록")
        List<AwardRecipientResponse> awardRecipients,
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
        String projectName,
        @Schema(description = "연계 과제 ID", example = "1")
        Long taskId,
        @Schema(description = "연계 과제명", example = "AI 기반 의료영상 분석 솔루션 개발")
        String taskName
) {
    public AwardResponse(Award award, List<AwardRecipient> recipients) {
        this(
                award.getId(),
                award.getRecipients(),
                recipients.stream().map(AwardRecipientResponse::new).collect(Collectors.toList()),
                award.getAwardDate(),
                award.getHostInstitution(),
                award.getCompetitionName(),
                award.getAwardName(),
                award.getPresentationTitle(),
                award.getProject().getId(),
                award.getProject().getTitle(),
                award.getTask() != null ? award.getTask().getId() : null,
                award.getTask() != null ? award.getTask().getTitle() : null
        );
    }

    public record AwardRecipientResponse(
            @Schema(description = "사용자 ID")
            Long userId,
            @Schema(description = "사용자 이름")
            String userName,
            @Schema(description = "역할")
            String role
    ) {
        public AwardRecipientResponse(AwardRecipient recipient) {
            this(
                    recipient.getUser().getId(),
                    recipient.getUser().getName(),
                    recipient.getRole()
            );
        }
    }
}
