package com.bmilab.backend.domain.project.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TimelineType {
    EXTERNAL_MEETING("외부 미팅"),
    INTERNAL_MEETING("내부 미팅"),
    SUBMISSION_DEADLINE("제출 마감"),
    MATERIAL_SHARE("자료 공유"),
    ETC("기타");

    private final String description;
}
