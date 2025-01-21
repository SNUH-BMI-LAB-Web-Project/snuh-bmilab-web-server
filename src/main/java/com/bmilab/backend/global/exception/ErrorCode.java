package com.bmilab.backend.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();
    String getMessage();
    HttpStatus getHttpStatus();
}
