package com.bmilab.backend.domain.leave.dto.response;

import com.bmilab.backend.domain.leave.entity.Leave;
import com.bmilab.backend.domain.leave.entity.UserLeave;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record UserLeaveResponse(
        @Schema(description = "총 연차 수", example = "15.0")
        Double annualLeaveCount,

        @Schema(description = "사용한 연차 수", example = "3.5")
        Double usedLeaveCount,

        List<LeaveDetail> leaves,

        Integer totalPage
) {

    public static UserLeaveResponse of(UserLeave userLeave, List<Leave> leaves, Integer totalPage) {

        return new UserLeaveResponse(
                userLeave.getAnnualLeaveCount(),
                userLeave.getUsedLeaveCount(),
                leaves.stream()
                        .map(LeaveDetail::from)
                        .toList(),
                totalPage
        );
    }
}
