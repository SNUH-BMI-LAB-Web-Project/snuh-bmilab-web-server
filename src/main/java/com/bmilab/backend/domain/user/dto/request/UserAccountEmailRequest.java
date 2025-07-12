package com.bmilab.backend.domain.user.dto.request;

import com.bmilab.backend.global.validation.RegExp;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserAccountEmailRequest(
        @Schema(name = "비밀번호", example = "SecurePass123!")
        @NotBlank(message = "비밀번호는 필수입니다.")
        @Pattern(regexp = RegExp.PASSWORD_EXPRESSION, message = RegExp.PASSWORD_MESSAGE)
        String password
) {
}
