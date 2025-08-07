package com.bmilab.backend.domain.leave.controller;

import com.bmilab.backend.domain.leave.dto.request.RejectLeaveRequest;
import com.bmilab.backend.domain.leave.dto.response.LeaveFindAllResponse;
import com.bmilab.backend.domain.leave.enums.LeaveStatus;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Tag(name = "(Admin) Leave", description = "관리자용 휴가 API")
public interface AdminLeaveApi {
    @Operation(summary = "휴가 승인", description = "휴가 요청을 승인하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "휴가 승인 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "휴가 정보를 찾을 수 없습니다."
                    )
            }
    )
    ResponseEntity<Void> approveLeave(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable long leaveId
    );

    @Operation(summary = "휴가 반려", description = "휴가 요청을 반려하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "휴가 반려 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "휴가 정보를 찾을 수 없습니다."
                    )
            }
    )
    ResponseEntity<Void> rejectLeave(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable long leaveId,
            RejectLeaveRequest request
    );

    @Operation(summary = "(관리자) 전체 휴가 조회", description = "휴가 정보를 모두 조회하거나 기간별로 조회할 수 있는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공"
                    )
            }
    )
    ResponseEntity<LeaveFindAllResponse> getLeaves(
            @RequestParam(required = false) LeaveStatus status,
            @PageableDefault(size = 10, sort = "applicatedAt", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable
    );
}
