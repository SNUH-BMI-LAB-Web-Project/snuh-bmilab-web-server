package com.bmilab.backend.domain.research.presentation.exception;

import com.bmilab.backend.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PresentationErrorCode implements ErrorCode {

    ACADEMIC_PRESENTATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 학술 발표를 찾을 수 없습니다."),
    ACADEMIC_PRESENTATION_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 학술 발표에 접근할 권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
