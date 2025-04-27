package com.bmilab.backend.domain.project.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MeetingType {
    @JsonProperty("정기 미팅")
    REGULAR("정기 미팅"),
    @JsonProperty("임시 미팅")
    AD_HOC("임시 미팅"),
    @JsonProperty("연구 발표")
    RESEARCH_PRESENTATION("연구 발표"),
    @JsonProperty("외부 협력")
    EXTERNAL_COLLABORATION("외부 협력");

    private final String description;
}
