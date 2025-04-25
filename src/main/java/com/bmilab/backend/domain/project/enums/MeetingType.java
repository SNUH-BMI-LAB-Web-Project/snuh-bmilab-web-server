package com.bmilab.backend.domain.project.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MeetingType {
    REGULAR("정기 미팅"), AD_HOC("임시 미팅"), RESEARCH_PRESENTATION("연구 발표"), EXTERNAL_COLLABORATION("외부 협력");

    private final String description;
}
