package com.bmilab.backend.domain.project.dto.response;

import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.entity.ProjectParticipant;
import com.bmilab.backend.domain.project.enums.ProjectCategory;
import com.bmilab.backend.domain.project.enums.ProjectStatus;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record ProjectDetail(
        @Schema(description = "연구 ID", example = "42")
        Long projectId,

        @Schema(description = "연구 생성자 정보")
        UserSummary author,

        @Schema(description = "연구 제목", example = "글로우업 플랫폼 확장 연구")
        String title,

        @Schema(description = "연구 설명", example = "AI 텍스트 작성 도구를 기반으로 한 협업 플랫폼 기능 확장")
        String content,

        @Schema(description = "연구 시작일", example = "2025-05-01")
        LocalDate startDate,

        @Schema(description = "연구 종료일", example = "2025-08-31")
        LocalDate endDate,

        @Schema(description = "연구 책임자 목록")
        List<UserSummary> leaders,

        @Schema(description = "연구 참여자 목록")
        List<UserSummary> participants,

        @Schema(description = "연구 분야", example = "NLP")
        ProjectCategory category,

        @Schema(description = "연구 상태", example = "IN_PROGRESS")
        ProjectStatus status,

        @Schema(description = "연구 IRB 번호", example = "IRB-DEF-...")
        String irbId,

        @Schema(description = "연구 DRB 번호", example = "DRB-DFEF-...")
        String drbId,

        @Schema(description = "첨부된 파일 URL 목록", example = "[\"https://s3.aws.com/project1/file1.png\", \"https://s3.aws.com/project1/file2.pdf\"]")
        List<String> fileUrls,

        @Schema(description = "연구 생성 시각", example = "2025-04-22T10:15:00")
        LocalDateTime createdAt
) {
    public static ProjectDetail from(Project project, List<ProjectParticipant> participants) {
        Map<Boolean, List<ProjectParticipant>> leaderPartitioned = participants.stream()
                .collect(Collectors.partitioningBy(ProjectParticipant::isLeader));

        return ProjectDetail
                .builder()
                .projectId(project.getId())
                .author(UserSummary.from(project.getAuthor()))
                .title(project.getTitle())
                .content(project.getContent())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .participants(
                        leaderPartitioned.get(false)
                                .stream()
                                .map(it -> UserSummary.from(it.getUser()))
                                .toList()
                )
                .leaders(
                        leaderPartitioned.get(true)
                                .stream()
                                .map(it -> UserSummary.from(it.getUser()))
                                .toList()
                )
                .category(project.getCategory())
                .status(project.getStatus())
                .irbId(project.getIrbId())
                .drbId(project.getDrbId())
                .fileUrls(project.getFileUrls())
                .createdAt(project.getCreatedAt())
                .build();
    }
}
