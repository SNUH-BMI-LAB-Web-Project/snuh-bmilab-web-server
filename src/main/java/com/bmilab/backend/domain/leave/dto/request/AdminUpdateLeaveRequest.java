package com.bmilab.backend.domain.leave.dto.request;

import com.bmilab.backend.domain.leave.enums.LeaveType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record AdminUpdateLeaveRequest(
        @Schema(description = "휴가 시작일")
        @NotNull(message = "휴가 시작일은 필수입니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @Schema(description = "휴가 종료일")
        @Nullable
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate,

        @Schema(description = "휴가 종류", example = "ANNUAL")
        @NotNull(message = "휴가 종류는 필수입니다.")
        LeaveType type,

        @Schema(description = "휴가 사유", example = "가족 행사 참석")
        @Nullable
        String reason
) {
}
