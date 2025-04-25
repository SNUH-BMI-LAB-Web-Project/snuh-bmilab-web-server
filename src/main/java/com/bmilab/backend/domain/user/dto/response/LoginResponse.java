package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        @Schema(description = "JWT 엑세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,

        @Schema(description = "로그인한 사용자 요약 정보")
        UserSummary user,

        @Schema(description = "사용자 역할", example = "USER")
        Role role
) {
    public static LoginResponse of(String accessToken, User user) {
        return new LoginResponse(accessToken, UserSummary.from(user), user.getRole());
    }
}
