package com.bmilab.backend.domain.research.publication.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthorType {
    CONTRIBUTION("기고"),
    BOOK("저서");

    private final String description;
}
