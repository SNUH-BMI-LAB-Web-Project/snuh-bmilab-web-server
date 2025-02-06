package com.bmilab.backend.domain.leave.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LeaveType {
    ANNUAL("연가"), SICK("병가"), HALF("반차");

    private String description;
}
