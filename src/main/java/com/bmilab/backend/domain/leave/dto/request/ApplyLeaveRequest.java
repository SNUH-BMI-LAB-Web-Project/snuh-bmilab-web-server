package com.bmilab.backend.domain.leave.dto.request;

import com.bmilab.backend.domain.leave.enums.LeaveType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record ApplyLeaveRequest(
        @Schema(description = "휴가 시작 일시", example = "2025-05-01T09:00:00")
        LocalDateTime startDate,

        @Schema(description = "휴가 종료 일시", example = "2025-05-02T18:00:00")
        LocalDateTime endDate,

        @Schema(description = "휴가 종류 (예: ANNUAL, SICK, HALF)", example = "ANNUAL")
        LeaveType type,

        @Schema(description = "휴가 사유", example = "가족 행사 참석")
        String reason
) {
}
