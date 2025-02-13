package com.bmilab.backend.domain.user.dto.request;

public record UpdateUserRequest(
        Double annualLeaveCount,
        String comment
) {
}
