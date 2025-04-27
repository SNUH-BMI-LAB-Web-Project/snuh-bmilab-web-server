package com.bmilab.backend.domain.project.dto.query;

import com.bmilab.backend.domain.project.enums.ProjectCategory;
import com.bmilab.backend.domain.project.enums.ProjectStatus;
import com.bmilab.backend.domain.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class GetAllProjectsQueryResult {
    private Long projectId;

    private String title;

    private ProjectCategory category;

    private LocalDate startDate;

    private LocalDate endDate;

    @Setter
    private List<User> leaders;

    private Long participantCount;

    private ProjectStatus status;

    private Boolean hasFile;
}
