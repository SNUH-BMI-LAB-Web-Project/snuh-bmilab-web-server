package com.bmilab.backend.domain.leave.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LeaveType {
    ANNUAL("연차"),
    HALF_AM("오전 반차"),
    HALF_PM("오후 반차"),
    SPECIAL_HALF_AM("특별 오전 반차"),
    SPECIAL_HALF_PM("특별 오후 반차"),
    SPECIAL_ANNUAL("특별 연차")
    ;

    private String description;

    public boolean isHalf() {
        return this == HALF_AM || this == HALF_PM;
    }
}
