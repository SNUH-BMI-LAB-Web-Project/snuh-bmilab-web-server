package com.bmilab.backend.domain.project.exception;

import com.bmilab.backend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProjectErrorCode implements ErrorCode {
    PROJECT_NOT_FOUND("연구를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PROJECT_ACCESS_DENIED("연구에 접근할 수 있는 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INVALID_RSS_DATA("유효하지 않은 RSS 응답 데이터를 받았습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    PROJECT_FILE_NOT_FOUND("연구 첨부파일 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    EXTERNAL_PROFESSOR_NOT_FOUND("외부 교수 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    EXTERNAL_PROFESSOR_DUPLICATE("외부 교수 정보가 이미 존재합니다.", HttpStatus.CONFLICT),
    IRB_FILES_IS_EMPTY("IRB 파일이 없습니다.", HttpStatus.NOT_FOUND),
    DRB_FILES_IS_EMPTY("DRB 파일이 없습니다.", HttpStatus.NOT_FOUND)
    ;

    private final String message;
    private final HttpStatus httpStatus;
}
