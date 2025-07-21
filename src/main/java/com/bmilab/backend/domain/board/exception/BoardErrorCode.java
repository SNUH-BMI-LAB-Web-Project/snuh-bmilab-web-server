package com.bmilab.backend.domain.board.exception;

import com.bmilab.backend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BoardErrorCode implements ErrorCode {
    BOARD_NOT_FOUND("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    BOARD_ACCESS_DENIED("해당 게시글에 접근할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    BOARD_FILE_NOT_FOUND("게시물 첨부파일 정보를 찾을 수 없습니다.",  HttpStatus.NOT_FOUND),;

    private final String message;
    private final HttpStatus httpStatus;
}
