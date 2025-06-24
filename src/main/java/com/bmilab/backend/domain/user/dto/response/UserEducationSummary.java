package com.bmilab.backend.domain.user.dto.response;

import com.bmilab.backend.domain.user.entity.UserEducation;
import java.time.YearMonth;
import lombok.Builder;

@Builder
public record UserEducationSummary(
        Long educationId,
        String title,
        YearMonth startYearMonth,
        YearMonth endYearMonth
) {
    public static UserEducationSummary from(UserEducation education) {
        return UserEducationSummary.builder()
                .educationId(education.getId())
                .title(education.getTitle())
                .startYearMonth(education.getStartYearMonth())
                .endYearMonth(education.getEndYearMonth())
                .build();
    }
}
