package com.bmilab.backend.domain.leave.dto.response;

import com.bmilab.backend.domain.leave.entity.Leave;
import java.util.List;

public record LeaveFindAllResponse(
        List<LeaveDetail> leaves
) {
    public static LeaveFindAllResponse of(List<Leave> leaves) {
        return new LeaveFindAllResponse(
                leaves.stream()
                        .map(LeaveDetail::from)
                        .toList()
        );
    }
}
