package com.bmilab.backend.domain.report.dto.response;

import com.bmilab.backend.domain.report.entity.UserReport;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record UserReportSummary(
        Long userReportId,
        Long reportId,
        UserSummary user,
        String fileUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserReportSummary from(UserReport userReport) {
        return UserReportSummary
                .builder()
                .userReportId(userReport.getId())
                .reportId(userReport.getReport().getId())
                .user(UserSummary.from(userReport.getUser()))
                .fileUrl(userReport.getFileUrl())
                .createdAt(userReport.getCreatedAt())
                .updatedAt(userReport.getUpdatedAt())
                .build();
    }
}
