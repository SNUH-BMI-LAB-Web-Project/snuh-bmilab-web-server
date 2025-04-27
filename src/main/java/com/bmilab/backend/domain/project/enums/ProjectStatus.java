package com.bmilab.backend.domain.project.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProjectStatus {
    @JsonProperty("진행 전")
    PENDING("진행 전"),
    @JsonProperty("진행 중")
    IN_PROGRESS("진행 중"),
    @JsonProperty("진행 종료")
    COMPLETED("진행 종료"),
    @JsonProperty("진행 대기")
    WAITING("진행 대기");

    private final String description;
}
