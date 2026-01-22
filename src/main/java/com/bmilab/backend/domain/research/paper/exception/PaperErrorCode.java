package com.bmilab.backend.domain.research.paper.exception;

import com.bmilab.backend.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaperErrorCode implements ErrorCode {

    PAPER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 논문을 찾을 수 없습니다."),
    JOURNAL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 저널을 찾을 수 없습니다."),
    PAPER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 논문에 접근할 권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
