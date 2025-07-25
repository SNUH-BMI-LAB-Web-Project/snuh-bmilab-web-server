package com.bmilab.backend.domain.comment.exception;

import com.bmilab.backend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements ErrorCode {
    COMMENT_NOT_FOUND("댓글 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    COMMENT_ACCESS_DENIED("댓글에 접근할 수 있는 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ;

    private final String message;
    private final HttpStatus httpStatus;
}