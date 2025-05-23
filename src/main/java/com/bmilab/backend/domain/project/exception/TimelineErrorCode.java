package com.bmilab.backend.domain.project.exception;

import com.bmilab.backend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TimelineErrorCode implements ErrorCode {
    TIMELINE_NOT_FOUND("연구 타임라인을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    TIMELINE_FILE_NOT_FOUND("타임라인 첨부파일 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND)
    ;

    private final String message;
    private final HttpStatus httpStatus;
}