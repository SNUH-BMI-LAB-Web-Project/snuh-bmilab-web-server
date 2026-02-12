package com.bmilab.backend.domain.leave.controller;

import com.bmilab.backend.domain.leave.dto.request.ApplyLeaveRequest;
import com.bmilab.backend.domain.leave.dto.response.LeaveFindAllResponse;
import com.bmilab.backend.domain.leave.dto.response.UserLeaveResponse;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Leave", description = "휴가 API")
public interface LeaveApi {
    @Operation(summary = "전체 휴가 조회", description = "휴가 정보를 모두 조회하거나 기간별로 조회할 수 있는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공"
                    )
            }
    )
    ResponseEntity<LeaveFindAllResponse> getLeaves(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    );

    @Operation(summary = "사용자 휴가 조회", description = "사용자의 휴가 정보를 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공"
                    )
            }
    )
    ResponseEntity<UserLeaveResponse> getLeavesByUser(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PageableDefault(size = 10, sort = "applicatedAt", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable
    );

    @Operation(summary = "휴가 신청", description = "사용자가 휴가를 신청하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "신청 성공"
                    )
            }
    )
    ResponseEntity<Void> applyLeave(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            ApplyLeaveRequest request
    );

    @Operation(summary = "휴가 취소", description = "대기 상태인 휴가를 취소하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "취소 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "대기 상태인 휴가만 취소할 수 있습니다."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "휴가 정보를 찾을 수 없습니다."
                    )
            }
    )
    ResponseEntity<Void> cancelLeave(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable long leaveId
    );
}
