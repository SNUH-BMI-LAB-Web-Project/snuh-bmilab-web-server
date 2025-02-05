package com.bmilab.backend.domain.leave.exception;

import com.bmilab.backend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LeaveErrorCode implements ErrorCode {
    LEAVE_NOT_FOUND("휴가 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    LEAVE_ALREADY_DONE("이미 처리된 휴가입니다.", HttpStatus.BAD_REQUEST),
    LEAVE_COUNT_REQUIRED("연가 수가 부족합니다.", HttpStatus.BAD_REQUEST),
    USER_LEAVE_NOT_FOUND("사용자의 휴가 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;
}
