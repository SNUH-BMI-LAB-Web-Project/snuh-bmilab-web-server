package com.bmilab.backend.global.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorResponse {
    @Schema(description = "에러 코드", example = "INTERNAL_SERVER_ERROR")
    private String code;

    @Schema(description = "에러 메시지", example = "에러 메시지 예시입니다.")
    private String message;

    @Schema(description = "HTTP 상태 코드", example = "500")
    private Integer status;

    @Schema(description = "에러 발생 시각 (UTC)", example = "2025-04-25T14:37:00Z")
    private Instant timestamp;

    public static ErrorResponse from(ErrorCode errorCode, Instant timestamp) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .status(errorCode.getHttpStatus().value())
                .timestamp(timestamp)
                .build();
    }

    public static ErrorResponse from(Exception e, Instant timestamp) {
        GlobalErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;

        return ErrorResponse
                .builder()
                .code(errorCode.name())
                .message(e.getClass().getSimpleName() + ": " + e.getMessage())
                .status(errorCode.getHttpStatus().value())
                .timestamp(timestamp)
                .build();
    }

    public static ErrorResponse from(Exception e, HttpStatus status, Instant timestamp) {
        return ErrorResponse
                .builder()
                .code(status.name())
                .message(e.getMessage())
                .status(status.value())
                .timestamp(timestamp)
                .build();
    }
}