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
}
