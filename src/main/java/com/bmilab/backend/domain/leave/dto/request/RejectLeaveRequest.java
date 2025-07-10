package com.bmilab.backend.domain.leave.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record RejectLeaveRequest(
        @Schema(description = "반려 사유", example = "연가 수 부족")
        @NotNull(message = "반려 사유는 필수 입력 항목입니다.")
        String rejectReason
) {
}
