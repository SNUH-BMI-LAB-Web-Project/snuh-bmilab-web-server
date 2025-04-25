package com.bmilab.backend.domain.user.dto.request;

import com.bmilab.backend.domain.project.enums.ProjectCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

public record RegisterUserRequest(
        @Schema(description = "사용자 이름", example = "홍길동")
        String name,

        @Schema(description = "이메일 주소", example = "hong.gildong@example.com")
        String email,

        @Schema(description = "비밀번호", example = "SecurePass123!")
        String password,

        @Schema(description = "소속 부서", example = "개발팀")
        String department,

        @Schema(description = "총 연차 개수", example = "15.0")
        Double annualLeaveCount,

        @Schema(description = "이미 사용한 연차 개수", example = "3.5")
        Double usedLeaveCount,

        @Schema(description = "연구 분야 목록", example = "[\"NLP\", \"BIOINFORMATICS\"]")
        List<ProjectCategory> categories,

        @Schema(description = "좌석 번호", example = "12-30")
        String seatNumber,

        @Schema(description = "전화번호", example = "010-1234-5678")
        String phoneNumber,

        @Schema(description = "입사일", example = "2025-04-01")
        LocalDate joinedAt
) {
}
