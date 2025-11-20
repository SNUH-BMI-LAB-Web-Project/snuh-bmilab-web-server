package com.bmilab.backend.domain.task.exception;

import com.bmilab.backend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TaskErrorCode implements ErrorCode {
    TASK_NOT_FOUND("과제를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    TASK_DUPLICATE_RESEARCH_NUMBER("이미 존재하는 연구과제번호입니다.", HttpStatus.CONFLICT),
    TASK_INVALID_YEAR("현재 연차는 총 연차보다 클 수 없습니다.", HttpStatus.BAD_REQUEST),
    TASK_CANNOT_EDIT("현재 상태에서는 과제를 수정할 수 없습니다.", HttpStatus.FORBIDDEN),
    TASK_PERIOD_NOT_FOUND("과제 기간 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    TASK_FILE_NOT_FOUND("과제 파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    TASK_MEMBER_NOT_FOUND("과제 참여자 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    TASK_PROJECT_NOT_FOUND("과제-프로젝트 연결 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    TASK_PROJECT_ALREADY_EXISTS("이미 연결된 프로젝트입니다.", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus httpStatus;
}
