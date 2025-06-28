package com.bmilab.backend.domain.project.dto.response;

import com.bmilab.backend.domain.project.dto.ExternalProfessorSummary;
import com.bmilab.backend.domain.project.dto.query.GetAllProjectsQueryResult;
import com.bmilab.backend.domain.project.enums.ProjectStatus;
import com.bmilab.backend.domain.projectcategory.dto.response.ProjectCategorySummary;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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

            @Schema(description = "연구 분야")
            ProjectCategorySummary category,

            @Schema(description = "연구 시작일", example = "2025-05-01")
            LocalDate startDate,

            @Schema(description = "연구 종료일", example = "2025-06-30")
            LocalDate endDate,

            @Schema(description = "PI 목록")
            List<ExternalProfessorSummary> piList,

            @Schema(description = "실무 교수 목록")
            List<ExternalProfessorSummary> practicalProfessors,

            @Schema(description = "연구 책임자 목록")
            List<UserSummary> leaders,

            @Schema(description = "연구 참여자 수", example = "8")
            int participantCount,

            @Schema(description = "연구 상태", example = "IN_PROGRESS")
            ProjectStatus status,

            @Schema(description = "연구 비공개 여부")
            boolean isPrivate,

            @Schema(description = "연구 접근 가능 여부")
            boolean isAccessible
    ) {
        public static ProjectSummary from(GetAllProjectsQueryResult queryResult) {
            String pi = queryResult.getPi();
            String practicalProfessor = queryResult.getPracticalProfessor();

            return ProjectSummary.builder()
                    .projectId(queryResult.getProjectId())
                    .title(queryResult.getTitle())
                    .category(ProjectCategorySummary.from(queryResult.getCategory()))
                    .startDate(queryResult.getStartDate())
                    .endDate(queryResult.getEndDate())
                    .piList(
                            pi == null ? List.of() :
                                    Arrays.stream(pi.split(","))
                                            .map(ExternalProfessorSummary::from).toList()
                    )
                    .practicalProfessors(
                            practicalProfessor == null ? List.of() :
                                    Arrays.stream(practicalProfessor.split(","))
                                            .map(ExternalProfessorSummary::from).toList()
                    )
                    .leaders(queryResult.getLeaders()
                            .stream()
                            .map(UserSummary::from)
                            .toList()
                    )
                    .participantCount(queryResult.getParticipantCount().intValue())
                    .status(queryResult.getStatus())
                    .isPrivate(queryResult.isPrivate())
                    .isAccessible(queryResult.isAccessible())
                    .build();
        }
    }
}
