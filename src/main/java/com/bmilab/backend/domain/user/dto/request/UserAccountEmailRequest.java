package com.bmilab.backend.domain.user.dto.request;

import com.bmilab.backend.global.validation.RegExp;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserAccountEmailRequest(
        @NotBlank(message = "비밀번호는 필수입니다.")
        @Pattern(regexp = RegExp.PASSWORD_EXPRESSION, message = RegExp.PASSWORD_MESSAGE)
        String password
) {
}
