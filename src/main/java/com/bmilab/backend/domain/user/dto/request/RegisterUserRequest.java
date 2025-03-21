package com.bmilab.backend.domain.user.dto.request;

public record RegisterUserRequest(
        String name,
        String email,
        String password,
        String department,
        Double annualLeaveCount,
        Double usedLeaveCount
) {
}
