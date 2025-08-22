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
    BOARD_FILE_NOT_FOUND("게시물 첨부파일 정보를 찾을 수 없습니다.",  HttpStatus.NOT_FOUND),
    BOARD_PIN_ACCESS_DENIED("게시글 고정 상태 변경 권한이 없습니다", HttpStatus.FORBIDDEN),
    CATEGORY_NOT_FOUND("존재하지 않는 게시판 분야입니다", HttpStatus.NOT_FOUND),
    CATEGORY_NAME_DUPLICATE("이미 존재하는 연구 분야 이름입니다.", HttpStatus.CONFLICT),
    BOARD_PIN_LIMIT_EXCEEDED("고정글은 최대 5개까지만 설정할 수 있습니다.",  HttpStatus.BAD_REQUEST),
    BOARD_PIN_VERSION_CONFLICT("동시에 다른 관리자가 게시글을 수정하여 충돌이 발생했습니다. 다시 시도해주세요.", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus httpStatus;
}