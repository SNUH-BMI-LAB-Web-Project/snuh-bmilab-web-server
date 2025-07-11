package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.user.entity.UserEducation;
import com.bmilab.backend.domain.user.enums.EducationType;
import com.bmilab.backend.domain.user.enums.EnrollmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.YearMonth;
import lombok.Builder;

@Builder
public record UserEducationSummary(
        @Schema(description = "사용자 학력 ID", example = "1")
        Long educationId,

        @Schema(description = "학교 또는 교육 과정명", example = "국민대학교 소프트웨어학부")
        String title,

        @Schema(description = "학적 상태 (재학, 휴학, 졸업)", example = "ENROLLED")
        EnrollmentStatus status,

        @Schema(description = "학력 구분")
        EducationType type,

        @Schema(type = "string", description = "시작 연월", example = "2020-03")
        @JsonFormat(pattern = "yyyy-MM")
        YearMonth startYearMonth,

        @Schema(type = "string", description = "종료 연월", example = "2024-02")
        @JsonFormat(pattern = "yyyy-MM")
        YearMonth endYearMonth
) {
    public static UserEducationSummary from(UserEducation education) {
        return UserEducationSummary.builder()
                .educationId(education.getId())
                .title(education.getTitle())
                .startYearMonth(education.getStartYearMonth())
                .endYearMonth(education.getEndYearMonth())
                .status(education.getStatus())
                .type(education.getType())
                .build();
    }
}
