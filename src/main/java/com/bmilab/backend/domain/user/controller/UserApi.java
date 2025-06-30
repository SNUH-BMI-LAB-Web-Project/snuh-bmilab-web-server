package com.bmilab.backend.domain.user.controller;

import com.bmilab.backend.domain.user.dto.request.UpdateUserPasswordRequest;
import com.bmilab.backend.domain.user.dto.request.UpdateUserRequest;
import com.bmilab.backend.domain.user.dto.request.UserEducationRequest;
import com.bmilab.backend.domain.user.dto.request.UserSearchConditionRequest;
import com.bmilab.backend.domain.user.dto.response.SearchUserResponse;
import com.bmilab.backend.domain.user.dto.response.UserDetail;
import com.bmilab.backend.domain.user.dto.response.UserFindAllResponse;
import com.bmilab.backend.global.annotation.FormDataRequestBody;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdAt", value = "criteria") String criteria);

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
    ResponseEntity<UserDetail> getCurrentUser(@AuthenticationPrincipal UserAuthInfo userAuthInfo);

    @Operation(summary = "현재 사용자 정보 수정", description = "현재 로그인한 사용자 정보를 수정하는 PUT API")
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
    @FormDataRequestBody
    ResponseEntity<Void> updateCurrentUser(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestPart(required = false) MultipartFile profileImage,
            @RequestPart UpdateUserRequest request
    );

    @Operation(summary = "비밀번호 변경", description = "현재 로그인한 사용자의 비밀번호를 변경하는 PATCH API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "비밀번호 변경 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "사용자 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> updatePassword(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody UpdateUserPasswordRequest request
    );

    @Operation(summary = "사용자 검색", description = "사용자 검색을 위한 조회용 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "사용자 조회 성공"
                    )
            }
    )
    ResponseEntity<SearchUserResponse> searchUsers(
            @ModelAttribute UserSearchConditionRequest request
    );

    @Operation(summary = "사용자 학력 추가", description = "사용자의 학력을 추가하기 위한 PATCH API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "사용자 학력 추가 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "사용자 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> addEducations(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody UserEducationRequest request
    );

    @Operation(summary = "사용자 학력 삭제", description = "사용자의 학력을 삭제하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "사용자 학력 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "사용자 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> deleteEducations(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long educationId
    );
}
