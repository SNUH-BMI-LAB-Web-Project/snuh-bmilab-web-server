package com.bmilab.backend.domain.project.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProjectStatus {
    PENDING("진행 전"),
    IN_PROGRESS("진행 중"),
    COMPLETED("진행 완료"),
    WAITING("진행 대기");

    private final String description;
}
