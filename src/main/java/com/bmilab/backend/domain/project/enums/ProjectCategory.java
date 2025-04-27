package com.bmilab.backend.domain.project.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProjectCategory {
    BIOINFORMATICS("Bioinformatics"),
    AI_PATHOLOGY("Medical AI (Pathology)"),
    AI_SIGNAL_DATA("Medical AI (Signal Data)"),
    BIG_DATA("Medical Big Data"),
    NLP("NLP");

    private final String description;
}
