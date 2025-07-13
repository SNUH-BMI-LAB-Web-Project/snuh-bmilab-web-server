package com.bmilab.backend.global.exception;

import java.io.IOException;
import java.time.Instant;

import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.sentry.Sentry;
import io.sentry.protocol.Request;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(ApiException exception) {

        ErrorCode errorCode = exception.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.from(errorCode, Instant.now());
        HttpStatus httpStatus = errorCode.getHttpStatus();

        log.error("에러 발생: ({}) {}", errorCode.name(), errorCode.getMessage());

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException exception) {

        return handleValidationException(exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        return handleValidationException(exception);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception, HttpServletRequest request) {

        ErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = ErrorResponse.from(exception, Instant.now());
        HttpStatus httpStatus = errorCode.getHttpStatus();

        exception.printStackTrace();

        sentry(exception, request);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private ResponseEntity<ErrorResponse> handleValidationException(BindException exception) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = ErrorResponse.from(exception, status, Instant.now());

        return ResponseEntity.status(status).body(errorResponse);
    }

    private void sentry(Throwable throwable, HttpServletRequest request) {

        Sentry.captureException(
                throwable, (scope) -> {
                    if (request.getUserPrincipal() instanceof UserAuthInfo userAuthInfo) {
                        User user = userAuthInfo.getUser();
                        io.sentry.protocol.User sentryUser = new io.sentry.protocol.User();

                        sentryUser.setId(user.getId().toString());
                        sentryUser.setEmail(user.getEmail());
                        sentryUser.setName(user.getName());
                        sentryUser.setIpAddress(request.getRemoteAddr());

                        scope.setUser(sentryUser);
                    }

                    Request sentryRequest = scope.getRequest();
                    if (sentryRequest != null) {
                        try {
                            String body = new String(
                                    request.getInputStream().readAllBytes(),
                                    request.getCharacterEncoding()
                            );

                            sentryRequest.setBodySize((long) body.length());
                            sentryRequest.setData(body);

                            scope.setRequest(sentryRequest);
                        } catch (IOException ignored) {
                        }
                    }
                }
        );
    }
}
