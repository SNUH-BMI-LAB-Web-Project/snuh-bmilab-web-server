package com.bmilab.backend.domain.research.presentation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AcademicPresentationType {
    ORAL("Oral"),
    MINI_ORAL("Mini oral"),
    POSTER("Poster");

    private final String description;
}
