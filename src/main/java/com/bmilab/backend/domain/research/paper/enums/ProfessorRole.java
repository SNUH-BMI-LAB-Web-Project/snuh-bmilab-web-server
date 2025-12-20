package com.bmilab.backend.domain.research.paper.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProfessorRole {
    FIRST_AUTHOR("제1저자"),
    CO_AUTHOR("공저자"),
    CORRESPONDING_AUTHOR("교신저자");

    private final String description;
}
