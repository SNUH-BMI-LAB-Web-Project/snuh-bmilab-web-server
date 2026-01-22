package com.bmilab.backend.domain.research.patent.exception;

import com.bmilab.backend.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PatentErrorCode implements ErrorCode {

    PATENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 특허 정보를 찾을 수 없습니다."),
    PATENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 특허 정보에 접근할 권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
