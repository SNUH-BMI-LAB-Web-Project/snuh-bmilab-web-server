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
    LEAVE_DUPLICATE("해당 날짜에 이미 신청된 휴가가 있습니다.", HttpStatus.CONFLICT),
    LEAVE_NOT_APPROVED("승인된 휴가만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST),
    LEAVE_COUNT_REQUIRED("연가 수가 부족합니다.", HttpStatus.BAD_REQUEST),
    USER_LEAVE_NOT_FOUND("사용자의 휴가 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    HOLIDAY_API_ERROR("공휴일 API 조회 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ACCESS_DENIED("신청 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
