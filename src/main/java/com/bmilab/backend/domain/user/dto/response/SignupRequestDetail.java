package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.user.entity.SignupRequestInfo;
import java.time.LocalDateTime;

public record SignupRequestDetail(
        Long requestId,
        String name,
        String email,
        String department,
        LocalDateTime requestedAt
) {
    public static SignupRequestDetail from(SignupRequestInfo signupRequestInfo) {
        return new SignupRequestDetail(
                signupRequestInfo.getId(),
                signupRequestInfo.getName(),
                signupRequestInfo.getEmail(),
                signupRequestInfo.getDepartment(),
                signupRequestInfo.getRequestedAt()
        );
    }
}
