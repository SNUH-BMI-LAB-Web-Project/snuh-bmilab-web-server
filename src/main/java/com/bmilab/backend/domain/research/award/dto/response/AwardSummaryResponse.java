package com.bmilab.backend.domain.research.award.dto.response;

import com.bmilab.backend.domain.research.award.entity.Award;
import com.bmilab.backend.domain.research.award.entity.AwardRecipient;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record AwardSummaryResponse(
        @Schema(description = "수상 ID")
        Long id,
        @Schema(description = "수상자 목록 (텍스트)")
        String recipients,
        @Schema(description = "연구실 내 수상자 목록")
        List<AwardResponse.AwardRecipientResponse> awardRecipients,
        @Schema(description = "수상 날짜")
        LocalDate awardDate,
        @Schema(description = "주최 기관")
        String hostInstitution,
        @Schema(description = "대회명")
        String competitionName,
        @Schema(description = "수상명")
        String awardName,
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
    public static AwardSummaryResponse from(Award award, List<AwardRecipient> recipients) {
        return new AwardSummaryResponse(
                award.getId(),
                award.getRecipients(),
                recipients.stream().map(AwardResponse.AwardRecipientResponse::new).collect(Collectors.toList()),
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
}
