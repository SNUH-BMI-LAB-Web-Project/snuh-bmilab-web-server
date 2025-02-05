package com.bmilab.backend.domain.report.controller;

import com.bmilab.backend.domain.report.dto.request.DeleteReportRequest;
import com.bmilab.backend.domain.report.dto.request.ReportRequest;
import com.bmilab.backend.domain.report.dto.response.ReportDetail;
import com.bmilab.backend.domain.report.dto.response.ReportFindAllResponse;
import com.bmilab.backend.domain.report.dto.response.UserReportFindAllResponse;
import com.bmilab.backend.global.exception.ErrorResponse;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Report", description = "주간보고서 API")
public interface ReportApi {
    @Operation(summary = "주간보고서 정보 생성", description = "주간보고서 정보를 생성하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "주간보고서 정보 생성 성공"
                    )
            }
    )
    ResponseEntity<Void> createReport(@RequestBody ReportRequest request);

    @Operation(summary = "주간보고서 정보 전체 조회", description = "모든 주간보고서 정보를 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "주간보고서 정보 조회 성공"
                    )
            }
    )
    ResponseEntity<ReportFindAllResponse> getAllReports(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            @RequestParam(required = false, defaultValue = "createdAt", value = "criteria") String criteria
    );

    @Operation(summary = "사용자 주간보고서 정보 조회", description = "사용자의 주간보고서 제출 정보를 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "주간보고서 정보 조회 성공"
                    )
            }
    )
    ResponseEntity<UserReportFindAllResponse> getAllReportsByUser(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo
    );

    @Operation(summary = "주간보고서 정보 상세 조회", description = "ID로 주간보고서 상세 정보를 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "주간보고서 정보 조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "주간보고서 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<ReportDetail> getReportById(@PathVariable Long reportId);

    @Operation(summary = "주간보고서 정보 수정", description = "ID로 주간보고서 정보를 수정하는 PUT API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "주간보고서 정보 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "주간보고서 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> updateReport(@PathVariable Long reportId, @RequestBody ReportRequest request);

    @Operation(summary = "주간보고서 정보 삭제", description = "주간보고서 정보를 삭제하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "주간보고서 정보 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "주간보고서 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> deleteReport(@RequestBody DeleteReportRequest request);

    @Operation(summary = "주간보고서 제출", description = "사용자가 주간보고서를 파일로 제출하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "주간보고서 제출 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "주간보고서 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> submitReport(@PathVariable Long reportId,
                                             @AuthenticationPrincipal UserAuthInfo principal,
                                             @RequestPart MultipartFile file);
}
