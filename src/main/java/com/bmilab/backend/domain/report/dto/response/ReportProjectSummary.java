package com.bmilab.backend.domain.report.dto.response;

import com.bmilab.backend.domain.project.entity.Project;
import lombok.Builder;

@Builder
public record ReportProjectSummary(
        Long projectId,
        String title
) {
    public static ReportProjectSummary from(Project project) {
        return ReportProjectSummary.builder()
                .projectId((project != null) ? project.getId() : null)
                .title((project != null) ? project.getTitle() : null)
                .build();
    }
}
