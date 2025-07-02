package com.bmilab.backend.domain.project.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectParticipantType {
    PARTICIPANT("연구실 내 참여자"),
    PRACTICAL_PROFESSOR("실무 교수"),
    PI("PI"),
    LEADER("연구실 내 책임자")
    ;

    private final String description;
}
