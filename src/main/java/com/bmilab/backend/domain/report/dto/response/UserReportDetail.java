package com.bmilab.backend.domain.report.dto.response;

import com.bmilab.backend.domain.report.entity.UserReport;
import com.bmilab.backend.domain.user.dto.response.UserDetail;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record UserReportDetail(
        Long userReportId,
        Long reportId,
        UserDetail user,
        String fileUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserReportDetail from(UserReport userReport) {
        return UserReportDetail
                .builder()
                .userReportId(userReport.getId())
                .reportId(userReport.getReport().getId())
                .user(UserDetail.from(userReport.getUser()))
                .fileUrl(userReport.getFileUrl())
                .createdAt(userReport.getCreatedAt())
                .updatedAt(userReport.getUpdatedAt())
                .build();
    }
}
