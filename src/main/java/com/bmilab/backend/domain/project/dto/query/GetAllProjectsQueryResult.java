package com.bmilab.backend.domain.project.dto.query;

import com.bmilab.backend.domain.project.enums.ProjectStatus;
import com.bmilab.backend.domain.projectcategory.entity.ProjectCategory;
import com.bmilab.backend.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class GetAllProjectsQueryResult {
    private Long projectId;

    private String title;

    private ProjectCategory category;

    private LocalDate startDate;

    private LocalDate endDate;

    private String pi;

    private String practicalProfessor;

    @Setter
    private List<User> leaders;

    private Long participantCount;

    private ProjectStatus status;

    private boolean isPrivate;

    private boolean isAccessible;

    private boolean isPinned;

    public ProjectStatus getEffectiveStatus() {
        if (this.status == ProjectStatus.WAITING) {
            return ProjectStatus.WAITING;
        }

        LocalDate today = LocalDate.now();

        if (this.endDate != null && today.isAfter(this.endDate)) {
            return ProjectStatus.COMPLETED;
        }

        if (this.startDate != null && !today.isBefore(this.startDate)) {
            return ProjectStatus.IN_PROGRESS;
        }

        return ProjectStatus.PENDING;
    }
}
