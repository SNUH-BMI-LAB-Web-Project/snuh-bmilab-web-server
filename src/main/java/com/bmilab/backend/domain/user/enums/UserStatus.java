package com.bmilab.backend.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    ACTIVE("재직"),
    ON_LEAVE("휴직"),
    RESIGNED("퇴사");

    private final String description;
}