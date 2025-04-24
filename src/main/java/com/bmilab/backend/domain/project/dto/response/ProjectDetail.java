package com.bmilab.backend.domain.project.dto.response;

import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.entity.ProjectParticipant;
import com.bmilab.backend.domain.project.enums.ProjectCategory;
import com.bmilab.backend.domain.project.enums.ProjectStatus;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record ProjectDetail(
        Long projectId,
        UserSummary author,
        String title,
        String content,
        LocalDate startDate,
        LocalDate endDate,
        List<UserSummary> leaders,
        List<UserSummary> participants,
        ProjectCategory category,
        ProjectStatus status,
        List<String> fileUrls,
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
                .fileUrls(project.getFileUrls())
                .createdAt(project.getCreatedAt())
                .build();
    }
}
