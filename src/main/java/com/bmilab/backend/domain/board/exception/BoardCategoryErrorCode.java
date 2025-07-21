package com.bmilab.backend.domain.board.exception;

import com.bmilab.backend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BoardCategoryErrorCode implements ErrorCode {
    CATEGORY_NOT_FOUND("존재하지 않는 게시판 분야입니다", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;

}
