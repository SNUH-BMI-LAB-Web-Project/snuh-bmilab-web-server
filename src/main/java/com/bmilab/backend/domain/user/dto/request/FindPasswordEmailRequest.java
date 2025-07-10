package com.bmilab.backend.domain.user.dto.request;

import com.bmilab.backend.global.validation.RegExp;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FindPasswordEmailRequest(
        @Schema(description = "이메일 주소", example = "hong.gildong@example.com")
        @NotBlank(message = "이메일은 필수입니다.")
        @Pattern(regexp = RegExp.EMAIL_EXPRESSION, message = RegExp.EMAIL_MESSAGE)
        @Size(max = 50, message = "이메일은 50자 이하로 입력해주세요.")
        String email
) {
}
