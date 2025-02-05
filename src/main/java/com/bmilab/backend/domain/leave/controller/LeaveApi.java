package com.bmilab.backend.domain.leave.controller;

import com.bmilab.backend.domain.leave.dto.request.ApplyLeaveRequest;
import com.bmilab.backend.domain.leave.dto.response.LeaveFindAllResponse;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate
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
    ResponseEntity<LeaveFindAllResponse> getLeavesByUser(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo
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
}
