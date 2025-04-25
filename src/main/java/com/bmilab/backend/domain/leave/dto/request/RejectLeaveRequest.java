package com.bmilab.backend.domain.leave.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record RejectLeaveRequest(
        @Schema(description = "반려 사유", example = "연가 수 부족")
        String rejectReason
) {
}
