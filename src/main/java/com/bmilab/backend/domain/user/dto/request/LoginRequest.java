package com.bmilab.backend.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "사용자 이메일 주소", example = "user@example.com")
        String email,

        @Schema(description = "사용자 비밀번호", example = "P@ssw0rd123!")
        String password
) {
}
