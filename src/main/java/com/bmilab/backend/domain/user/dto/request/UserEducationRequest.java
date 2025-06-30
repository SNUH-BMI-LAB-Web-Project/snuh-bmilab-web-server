package com.bmilab.backend.domain.user.dto.request;

import com.bmilab.backend.domain.user.enums.EnrollmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.YearMonth;

public record UserEducationRequest(
        @Schema(description = "학교 또는 교육 과정명", example = "국민대학교 소프트웨어학부")
        String title,

        @Schema(description = "학적 상태 (재학, 휴학, 졸업)", example = "ENROLLED")
        EnrollmentStatus status,

        @Schema(type = "string", description = "시작 연월", example = "2020-03")
        @JsonFormat(pattern = "yyyy-MM")
        YearMonth startYearMonth,

        @Schema(type = "string", description = "종료 연월", example = "2024-02")
        @JsonFormat(pattern = "yyyy-MM")
        YearMonth endYearMonth
) {
}
