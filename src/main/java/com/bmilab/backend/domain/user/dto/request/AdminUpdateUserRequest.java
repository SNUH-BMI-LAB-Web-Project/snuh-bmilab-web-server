package com.bmilab.backend.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record AdminUpdateUserRequest(
        @Schema(description = "연가 개수", example = "14.0")
        Double annualLeaveCount,
        @Schema(description = "비고", example = "특이사항 없음")
        String comment
) {
}
