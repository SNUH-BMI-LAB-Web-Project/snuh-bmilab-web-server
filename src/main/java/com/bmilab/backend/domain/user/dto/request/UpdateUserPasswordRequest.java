package com.bmilab.backend.domain.user.dto.request;

public record UpdateUserPasswordRequest(
        String currentPassword,
        String newPassword
) {
}
