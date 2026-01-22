package com.bmilab.backend.domain.research.paper.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JournalCategory {
    SCI("SCI"),
    SCIE("SCIE"),
    SCOPUS("SCOPUS"),
    ESCI("ESCI");

    private final String description;
}
