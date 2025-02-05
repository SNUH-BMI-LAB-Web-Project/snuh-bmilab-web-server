package com.bmilab.backend.domain.leave.dto.response;

import com.bmilab.backend.domain.leave.entity.Leave;
import com.bmilab.backend.domain.leave.enums.LeaveStatus;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record LeaveDetail(
        Long leaveId,
        UserSummary user,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LeaveStatus status,
        String reason,
        LocalDateTime applicatedAt
) {
    public static LeaveDetail from(Leave leave) {
        return LeaveDetail
                .builder()
                .leaveId(leave.getId())
                .user(UserSummary.from(leave.getUser()))
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .status(leave.getStatus())
                .reason(leave.getReason())
                .applicatedAt(leave.getApplicatedAt())
                .build();
    }
}
