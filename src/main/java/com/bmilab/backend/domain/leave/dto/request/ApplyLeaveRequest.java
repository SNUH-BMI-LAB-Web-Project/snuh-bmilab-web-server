package com.bmilab.backend.domain.leave.dto.request;

import com.bmilab.backend.domain.leave.enums.LeaveType;
import java.time.LocalDateTime;

public record ApplyLeaveRequest(
        LocalDateTime startDate,
        LocalDateTime endDate,
        LeaveType type,
        String reason
) {
}
