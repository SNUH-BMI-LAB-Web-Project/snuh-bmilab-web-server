package com.bmilab.backend.domain.user.dto.request;

import com.bmilab.backend.domain.user.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusRequest(
        @Schema(description = "사용자 상태", example = "RESIGNED")
        @NotNull(message = "사용자 상태는 필수입니다.")
        UserStatus status
) {
}