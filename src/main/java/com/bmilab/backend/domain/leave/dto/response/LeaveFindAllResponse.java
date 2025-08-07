package com.bmilab.backend.domain.leave.dto.response;

import com.bmilab.backend.domain.leave.entity.Leave;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record LeaveFindAllResponse(
        List<LeaveDetail> leaves,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Integer totalPage
) {
    public static LeaveFindAllResponse of(List<Leave> leaves) {
        return new LeaveFindAllResponse(
                leaves.stream()
                        .map(LeaveDetail::from)
                        .toList(),
                null
        );
    }

    public static LeaveFindAllResponse of(List<Leave> leaves, Integer totalPage) {
        return new LeaveFindAllResponse(
                leaves.stream()
                        .map(LeaveDetail::from)
                        .toList(),
                totalPage
        );
    }
}
