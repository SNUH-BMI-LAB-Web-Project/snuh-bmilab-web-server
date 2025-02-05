package com.bmilab.backend.domain.leave.controller;

import com.bmilab.backend.domain.leave.dto.request.RejectLeaveRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

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
    ResponseEntity<Void> approveLeave(@PathVariable long leaveId);

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
    ResponseEntity<Void> rejectLeave(@PathVariable long leaveId, RejectLeaveRequest request);
}
