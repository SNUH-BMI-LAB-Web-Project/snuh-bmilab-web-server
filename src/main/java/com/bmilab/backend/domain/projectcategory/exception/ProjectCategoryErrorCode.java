package com.bmilab.backend.domain.projectcategory.exception;

import com.bmilab.backend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProjectCategoryErrorCode implements ErrorCode {
    CATEGORY_NOT_FOUND("존재하지 않는 연구 분야입니다.", HttpStatus.NOT_FOUND),
    CATEGORY_NAME_DUPLICATE("이미 존재하는 연구 분야 이름입니다.", HttpStatus.CONFLICT)
    ;

    private final String message;
    private final HttpStatus httpStatus;
}