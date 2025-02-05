package com.bmilab.backend.domain.report.dto.response;

import com.bmilab.backend.domain.report.entity.UserReport;
import java.util.List;

public record UserReportFindAllResponse(
        List<UserReportDetail> userReports
) {
    public static UserReportFindAllResponse of(List<UserReport> userReports) {
        return new UserReportFindAllResponse(
                userReports
                        .stream()
                        .map(UserReportDetail::from)
                        .toList()
        );
    }
}
