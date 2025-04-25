package com.bmilab.backend.domain.user.dto.request;

import com.bmilab.backend.domain.project.enums.ProjectCategory;
import java.util.List;

public record UpdateUserRequest(
        String name,
        String email,
        String department,
        List<ProjectCategory> categories,
        String phoneNumber,
        String seatNumber
) {
}
