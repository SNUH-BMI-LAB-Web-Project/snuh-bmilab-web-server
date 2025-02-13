package com.bmilab.backend.domain.leave.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LeaveType {
    ANNUAL("연가"), SICK("병가"), HALF("반차"), ETC("기타");

    private String description;
}
