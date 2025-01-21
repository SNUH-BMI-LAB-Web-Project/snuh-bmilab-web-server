package com.bmilab.backend.domain.user.dto.request;

public record ApproveSignupRequest(
        Long requestId,
        Integer leaveCount
) {
}
