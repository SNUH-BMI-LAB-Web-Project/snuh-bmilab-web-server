package com.bmilab.backend.domain.user.dto.request;

public record AdminUpdateUserRequest(
        Double annualLeaveCount,
        String comment
) {
}
