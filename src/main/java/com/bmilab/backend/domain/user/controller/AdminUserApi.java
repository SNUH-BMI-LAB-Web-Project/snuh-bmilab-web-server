package com.bmilab.backend.domain.user.controller;

import com.bmilab.backend.domain.user.dto.request.RegisterUserRequest;
import com.bmilab.backend.domain.user.dto.request.AdminUpdateUserRequest;
import com.bmilab.backend.domain.user.dto.response.UserDetail;
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

@Tag(name = "(Admin) Auth", description = "관리자용 인증 처리 API")
public interface AdminUserApi {
    @Operation(summary = "신규 사용자 등록", description = "신규 사용자를 등록하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "사용자 등록 성공"
                    )
            }
    )
    ResponseEntity<Void> registerNewUser(@RequestBody RegisterUserRequest request);

    @Operation(summary = "사용자 정보 상세 조회", description = "ID로 사용자 정보를 상세 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "사용자 정보 상세 조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "사용자 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<UserDetail> getUserById(@PathVariable Long userId);

    @Operation(summary = "사용자 정보 수정", description = "ID로 사용자 비고 메시지와 휴가 수를 수정하는 PATCH API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "사용자 정보 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "사용자 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> updateUserById(@PathVariable Long userId, @RequestBody AdminUpdateUserRequest request);

    @Operation(summary = "사용자 삭제", description = "ID로 사용자를 삭제하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "사용자 정보 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "사용자 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> deleteUserById(@PathVariable Long userId);
}
