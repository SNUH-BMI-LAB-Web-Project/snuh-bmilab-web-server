package com.bmilab.backend.domain.user.controller;

import com.bmilab.backend.domain.user.dto.request.LoginRequest;
import com.bmilab.backend.domain.user.dto.request.SignupRequest;
import com.bmilab.backend.domain.user.dto.response.LoginResponse;
import com.bmilab.backend.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "로그인/회원가입 API")
public interface AuthApi {
    @Operation(summary = "로그인", description = "이메일/비밀번호로 로그인하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그인 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "사용자 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "비밀번호가 일치하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request);

    @Operation(summary = "회원가입 요청", description = "회원가입 요청하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원가입 요청 성공"
                    )
            }
    )
    ResponseEntity<Void> requestSignup(@RequestBody SignupRequest request);
}
