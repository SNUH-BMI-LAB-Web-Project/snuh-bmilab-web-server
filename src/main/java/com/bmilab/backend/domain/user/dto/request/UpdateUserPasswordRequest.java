package com.bmilab.backend.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateUserPasswordRequest(
        @Schema(description = "현재 비밀번호", example = "OldPass123!")
        String currentPassword,

        @Schema(description = "새 비밀번호", example = "NewPass456!")
        String newPassword
) {
}
