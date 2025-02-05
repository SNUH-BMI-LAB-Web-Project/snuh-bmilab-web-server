package com.bmilab.backend.domain.user.dto.request;

import java.time.LocalDateTime;

public record ApproveSignupRequest(
        Long requestId,
        LocalDateTime joinedAt,
        Double annualLeaveCount,
        Double usedLeaveCount
) {
}
