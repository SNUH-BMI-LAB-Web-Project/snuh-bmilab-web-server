package com.bmilab.backend.domain.project.dto.response;

import com.bmilab.backend.domain.project.dto.query.GetAllProjectsQueryResult;
import com.bmilab.backend.domain.project.enums.ProjectCategory;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
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
            Long projectId,
            String title,
            ProjectCategory category,
            LocalDate startDate,
            LocalDate endDate,
            List<UserSummary> leaders,
            int participantCount,
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
                    .hasFile(queryResult.getHasFile())
                    .build();
        }
    }
}
