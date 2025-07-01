package com.bmilab.backend.domain.project.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectSortOption {
    START_DATE_DESC("연구 시작일 최신순"),
    START_DATE_ASC("연구 시작일 오래된 순"),
    END_DATE_DESC("연구 종료일 최신순"),
    END_DATE_ASC("연구 종료일 오래된 순")
    ;

    private final String description;
}