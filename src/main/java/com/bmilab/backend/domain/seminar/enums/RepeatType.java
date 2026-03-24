package com.bmilab.backend.domain.seminar.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RepeatType {
    WEEKLY("매주"),
    MONTHLY("매월");

    private final String description;
}
