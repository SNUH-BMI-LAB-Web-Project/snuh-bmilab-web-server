package com.bmilab.backend.domain.user.dto.request;

import com.bmilab.backend.global.validation.RegExp;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateUserPasswordRequest(
        @Schema(description = "현재 비밀번호", example = "OldPass123!")
        @NotBlank(message = "현재 비밀번호는 필수입니다.")
        @Pattern(regexp = RegExp.PASSWORD_EXPRESSION, message = RegExp.PASSWORD_MESSAGE)
        String currentPassword,

        @Schema(description = "새 비밀번호", example = "NewPass456!")
        @NotBlank(message = "새 비밀번호는 필수입니다.")
        @Pattern(regexp = RegExp.PASSWORD_EXPRESSION, message = RegExp.PASSWORD_MESSAGE)
        String newPassword
) {
}
