package com.bmilab.backend.domain.report.controller;

import com.bmilab.backend.domain.report.dto.request.ReportRequest;
import com.bmilab.backend.domain.report.dto.response.ReportFindAllResponse;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Tag(name = "Report", description = "일일 업무 보고 API")
public interface ReportApi {
    @Operation(summary = "일일 업무 보고 생성", description = "새로운 일일 업무 보고를 생성하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "일일 업무 보고 생성 성공"
                    ),
            }
    )
    ResponseEntity<Void> createReport(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody ReportRequest request
    );

    @Operation(summary = "일일 업무 보고 수정", description = "일일 업무 보고를 수정하는 PUT API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "일일 업무 보고 수정 성공"
                    ),
            }
    )
    ResponseEntity<Void> updateReport(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long reportId,
            @RequestBody ReportRequest request
    );

    @Operation(summary = "일일 업무 보고 삭제", description = "일일 업무 보고를 삭제하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "일일 업무 보고 삭제 성공"
                    ),
            }
    )
    ResponseEntity<Void> deleteReport(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long reportId
    );

    @Operation(summary = "내 일일 업무 보고 조회", description = "현재 로그인한 사용자의 일일 업무 보고를 조회하는 API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "내 업무 보고 조회 성공"
                    ),
            }
    )
    ResponseEntity<ReportFindAllResponse> getReportsByCurrentUser(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    );
}
