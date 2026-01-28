package com.bmilab.backend.domain.seminar.exception;

import com.bmilab.backend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SeminarErrorCode implements ErrorCode {
    SEMINAR_NOT_FOUND("세미나/학회 일정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ACCESS_DENIED("수정/삭제 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
