package com.bmilab.backend.domain.project.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProjectCategory {
    @JsonProperty("Bioinformatics")
    BIOINFORMATICS("Bioinformatics"),
    @JsonProperty("Medical AI (Pathology)")
    AI_PATHOLOGY("Medical AI (Pathology)"),
    @JsonProperty("Medical AI (Signal Data)")
    AI_SIGNAL_DATA("Medical AI (Signal Data)"),
    @JsonProperty("Medical Big Data")
    BIG_DATA("Medical Big Data"),
    NLP("NLP");

    private final String description;
}
