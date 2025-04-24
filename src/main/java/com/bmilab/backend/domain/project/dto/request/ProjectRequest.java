package com.bmilab.backend.domain.project.dto.request;

import com.bmilab.backend.domain.project.enums.ProjectCategory;
import java.time.LocalDate;
import java.util.List;

public record ProjectRequest(
        String title,
        String content,
        List<Long> leaderIds,
        List<Long> participantIds,
        LocalDate startDate,
        LocalDate endDate,
        boolean isWaiting,
        ProjectCategory category
) {
}
