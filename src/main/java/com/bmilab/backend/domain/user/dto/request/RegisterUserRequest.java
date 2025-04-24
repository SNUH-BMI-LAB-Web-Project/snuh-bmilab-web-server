package com.bmilab.backend.domain.user.dto.request;

import com.bmilab.backend.domain.project.enums.ProjectCategory;
import java.time.LocalDate;
import java.util.List;

public record RegisterUserRequest(
        String name,
        String email,
        String password,
        String department,
        Double annualLeaveCount,
        Double usedLeaveCount,
        List<ProjectCategory> categories,
        String seatNumber,
        String phoneNumber,
        LocalDate joinedAt
) {
}
