package com.bmilab.backend.domain.project.dto.response;

import com.bmilab.backend.domain.project.dto.query.GetAllProjectsQueryResult;
import com.bmilab.backend.domain.project.enums.ProjectCategory;
import com.bmilab.backend.domain.project.enums.ProjectStatus;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record ProjectFindAllResponse(
        List<ProjectSummary> projects,
        int totalPage
) {
    @Builder
    public record ProjectSummary(

            @Schema(description = "연구 ID", example = "42")
            Long projectId,

            @Schema(description = "연구 제목", example = "AI 콘텐츠 자동 생성 시스템")
            String title,

            @Schema(description = "연구 분야", example = "NLP")
            ProjectCategory category,

            @Schema(description = "연구 시작일", example = "2025-05-01")
            LocalDate startDate,

            @Schema(description = "연구 종료일", example = "2025-06-30")
            LocalDate endDate,

            @Schema(description = "연구 책임자 목록")
            List<UserSummary> leaders,

            @Schema(description = "연구 참여자 수", example = "8")
            int participantCount,

            @Schema(description = "연구 상태", example = "IN_PROGRESS")
            ProjectStatus status,

            @Schema(description = "첨부 파일 존재 여부", example = "true")
            boolean hasFile
    ) {
        public static ProjectSummary from(GetAllProjectsQueryResult queryResult) {
            return ProjectSummary.builder()
                    .projectId(queryResult.getProjectId())
                    .title(queryResult.getTitle())
                    .category(queryResult.getCategory())
                    .startDate(queryResult.getStartDate())
                    .endDate(queryResult.getEndDate())
                    .leaders(queryResult.getLeaders()
                            .stream()
                            .map(UserSummary::from)
                            .toList()
                    )
                    .participantCount(queryResult.getParticipantCount().intValue())
                    .status(queryResult.getStatus())
                    .hasFile(queryResult.getHasFile())
                    .build();
        }
    }
}
