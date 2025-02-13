package com.bmilab.backend.domain.user.controller;

import com.bmilab.backend.domain.user.dto.request.UpdateUserRequest;
import com.bmilab.backend.domain.user.dto.response.CurrentUserDetail;
import com.bmilab.backend.domain.user.dto.response.UserDetail;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import com.bmilab.backend.domain.user.dto.response.UserFindAllResponse;
import com.bmilab.backend.global.exception.ErrorResponse;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "User", description = "사용자 조회 API")
public interface UserApi {
    @Operation(summary = "전체 사용자 정보 조회", description = "전체 사용자 정보를 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "전체 사용자 정보 조회 성공"
                    )
            }
    )
    ResponseEntity<UserFindAllResponse> getAllUsers(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            @RequestParam(required = false, defaultValue = "createdAt", value = "criteria") String criteria);

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

    @Operation(summary = "현재 사용자 정보 상세 조회", description = "현재 로그인한 사용자 정보를 상세 조회하는 GET API")
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
    ResponseEntity<CurrentUserDetail> getCurrentUser(@AuthenticationPrincipal UserAuthInfo userAuthInfo);

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
    ResponseEntity<Void> updateUserById(@PathVariable Long userId, @RequestBody UpdateUserRequest request);
}
