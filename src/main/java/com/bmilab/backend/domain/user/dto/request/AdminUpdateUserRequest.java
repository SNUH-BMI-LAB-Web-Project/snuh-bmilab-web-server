package com.bmilab.backend.domain.user.dto.request;

import com.bmilab.backend.domain.user.enums.Role;
import com.bmilab.backend.domain.user.enums.UserAffiliation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record AdminUpdateUserRequest(
        @Schema(description = "사용자 이름", example = "홍길동")
        String name,

        @Schema(description = "이메일 주소", example = "hong.gildong@example.com")
        String email,

        @Schema(description = "기관", example = "융합의학연구실")
        String organization,

        @Schema(description = "부서", example = "개발팀")
        String department,

        @Schema(description = "소속 (있으면)", example = "MASTERS_STUDENT")
        UserAffiliation affiliation,

        @Schema(description = "권한")
        Role role,

        @Schema(description = "추가된 연구 분야 ID 목록", example = "[1, 2, 3]")
        List<Long> newCategoryIds,

        @Schema(description = "삭제된 연구 분야 ID 목록", example = "[1, 2, 3]")
        List<Long> deletedCategoryIds,

        @Schema(description = "전화번호", example = "010-5678-1234")
        String phoneNumber,

        @Schema(description = "좌석 번호", example = "14-07")
        String seatNumber,

        @Schema(description = "연가 개수", example = "14.0")
        Double annualLeaveCount,

        @Schema(description = "비고", example = "특이사항 없음")
        String comment
) {
}
