package com.bmilab.backend.domain.research.award.exception;

import com.bmilab.backend.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AwardErrorCode implements ErrorCode {

    AWARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 수상 정보를 찾을 수 없습니다."),
    AWARD_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 수상 정보에 접근할 권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
