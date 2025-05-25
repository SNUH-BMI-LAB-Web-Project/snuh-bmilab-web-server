package com.bmilab.backend.domain.file.exception;

import com.bmilab.backend.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FileErrorCode implements ErrorCode {
    FILE_NOT_FOUND("첨부파일 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FILE_DOMAIN_MISMATCH("삭제하려는 첨부파일의 도메인이 일치하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
