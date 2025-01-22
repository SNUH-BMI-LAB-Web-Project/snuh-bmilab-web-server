package com.bmilab.backend.domain.report.exception;

import com.bmilab.backend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReportErrorCode implements ErrorCode {
    REPORT_NOT_FOUND("보고서 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;
}