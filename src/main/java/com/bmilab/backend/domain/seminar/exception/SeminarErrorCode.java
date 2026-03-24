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
    INVALID_REPEAT_OPTIONS("반복 종료일은 반복 유형이 지정된 경우 필수이며, 시작일 이후여야 합니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
