package com.bmilab.backend.domain.user.dto.request;

import com.bmilab.backend.domain.user.enums.EnrollmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.YearMonth;

public record UserEducationRequest(
        @Schema(description = "학교 또는 교육 과정명", example = "국민대학교 소프트웨어학부")
        @NotNull(message = "학교 또는 교육 과정명은 필수입니다.")
        @Size(max = 30, message = "학교 또는 교육 과정명은 30자 이하로 입력해주세요.")
        String title,

        @Schema(description = "학적 상태 (재학, 휴학, 졸업)", example = "ENROLLED")
        @NotNull(message = "학적 상태는 필수입니다.")
        EnrollmentStatus status,

        @Schema(type = "string", description = "시작 연월", example = "2020-03")
        @JsonFormat(pattern = "yyyy-MM")
        @DateTimeFormat(pattern = "yyyy-MM")
        @NotNull(message = "시작 연월은 필수입니다.")
        YearMonth startYearMonth,

        @Schema(type = "string", description = "종료 연월", example = "2024-02")
        @JsonFormat(pattern = "yyyy-MM")
        @DateTimeFormat(pattern = "yyyy-MM")
        @NotNull(message = "종료 연월은 필수입니다.")
        YearMonth endYearMonth
) {
}
