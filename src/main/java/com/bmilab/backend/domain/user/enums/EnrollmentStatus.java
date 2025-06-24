package com.bmilab.backend.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EnrollmentStatus {
    ENROLLED("재학 중"),
    LEAVE_OF_ABSENCE("휴학"),
    GRADUATED("졸업")
    ;

    private final String description;
}
