package com.bmilab.backend.domain.project.event;

import com.bmilab.backend.domain.project.entity.Project;

public record ProjectUpdateEvent(
        Project project
) {
}
