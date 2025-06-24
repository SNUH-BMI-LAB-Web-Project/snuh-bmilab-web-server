package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.user.entity.UserEducation;
import com.bmilab.backend.domain.user.enums.EnrollmentStatus;
import java.time.YearMonth;
import lombok.Builder;

@Builder
public record UserEducationSummary(
        Long educationId,
        String title,
        YearMonth startYearMonth,
        YearMonth endYearMonth,
        EnrollmentStatus status
) {
    public static UserEducationSummary from(UserEducation education) {
        return UserEducationSummary.builder()
                .educationId(education.getId())
                .title(education.getTitle())
                .startYearMonth(education.getStartYearMonth())
                .endYearMonth(education.getEndYearMonth())
                .status(education.getStatus())
                .build();
    }
}
