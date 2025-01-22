package com.bmilab.backend.domain.user.controller;

import com.bmilab.backend.domain.user.dto.request.ApproveSignupRequest;
import com.bmilab.backend.domain.user.dto.response.SignupRequestFindAllResponse;
import com.bmilab.backend.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Auth(Admin)", description = "관리자용 인증 처리 API")
public interface AdminApi {
    @Operation(summary = "전체 회원가입 요청 조회", description = "전체 회원가입 요청을 조회하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원가입 요청 조회 성공"
                    )
            }
    )
    ResponseEntity<SignupRequestFindAllResponse> getAllSignupRequests(
            @RequestParam boolean isPending
    );

    @Operation(summary = "회원가입 요청 승인", description = "회원가입 요청을 승인하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원가입 요청 승인 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "회원가입 요청 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> approveSignup(@RequestBody ApproveSignupRequest request);

    @Operation(summary = "회원가입 요청 거절", description = "회원가입 요청을 거절하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "회원가입 요청 거절 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "회원가입 요청 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> rejectSignup(@PathVariable Long requestId);
}
