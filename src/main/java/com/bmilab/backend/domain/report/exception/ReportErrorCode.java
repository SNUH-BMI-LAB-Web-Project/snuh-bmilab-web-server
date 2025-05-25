package com.bmilab.backend.domain.report.exception;

import com.bmilab.backend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReportErrorCode implements ErrorCode {
    REPORT_NOT_FOUND("일일 보고 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    REPORT_ACCESS_DENIED("해당 일일 보고에 접근할 권한이 없습니다.", HttpStatus.FORBIDDEN)
    ;

    private final String message;
    private final HttpStatus httpStatus;
}