package com.bmilab.backend.domain.seminar.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SeminarLabel {
    SEMINAR("세미나"),
    CONFERENCE("학회");

    private final String description;
}
