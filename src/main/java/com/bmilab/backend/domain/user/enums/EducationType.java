package com.bmilab.backend.domain.user.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EducationType {
    HIGH_SCHOOL("고등학교"),
    BACHELOR("학사"),
    MASTER("석사"),
    DOCTORATE("박사"),
    MASTER_DOCTORATE("석박통합");

    private final String description;
}
