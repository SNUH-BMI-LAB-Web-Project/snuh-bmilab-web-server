package com.bmilab.backend.domain.leave.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LeaveStatus {
    PENDING("대기 중"), APPROVED("승인"), REJECTED("반려");

    private String description;
}
