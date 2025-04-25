package com.bmilab.backend.domain.user.exception;

import com.bmilab.backend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND("사용자 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PASSWORD_MISMATCH("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    SIGNUP_REQUEST_NOT_FOUND("회원가입 요청 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    SIGNUP_REQUEST_ALREADY_DONE("이미 처리된 회원가입 요청입니다.", HttpStatus.BAD_REQUEST),
    SAME_AS_CURRENT_PASSWORD("새 비밀번호가 현재 비밀번호와 같습니다.", HttpStatus.BAD_REQUEST),
    INVALID_PROFILE_IMAGE_FILE_TYPE("프로필 이미지의 파일 형식이 잘못되었습니다.", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;
}