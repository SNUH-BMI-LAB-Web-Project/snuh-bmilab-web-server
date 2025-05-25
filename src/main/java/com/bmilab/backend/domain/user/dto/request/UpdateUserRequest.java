package com.bmilab.backend.domain.user.dto.request;

import com.bmilab.backend.domain.project.enums.ProjectCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record UpdateUserRequest(
        @Schema(description = "사용자 이름", example = "홍길동")
        String name,

        @Schema(description = "이메일 주소", example = "hong.gildong@example.com")
        String email,

        @Schema(description = "기관", example = "융합의학연구실")
        String organization,

        @Schema(description = "부서", example = "개발팀")
        String department,

        @Schema(description = "소속 (있으면)", example = "소속")
        String affiliation,

        @Schema(description = "연구 분야 목록", example = "[\"NLP\", \"BIOINFORMATICS\"]")
        List<ProjectCategory> categories,

        @Schema(description = "전화번호", example = "010-5678-1234")
        String phoneNumber,

        @Schema(description = "좌석 번호", example = "14-07")
        String seatNumber,

        @Schema(description = "학력", example = "국민대학교 소프트웨어학부 재학 중")
        String education
) {
}
