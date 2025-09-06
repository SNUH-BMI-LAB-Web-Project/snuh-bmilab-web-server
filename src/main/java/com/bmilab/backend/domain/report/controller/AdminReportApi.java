package com.bmilab.backend.domain.report.controller;

import com.bmilab.backend.domain.report.dto.response.ReportFindAllResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Tag(name = "(Admin)Report", description = "(관리자용) 일일 업무 보고 API")
public interface AdminReportApi {
    @Operation(summary = "일일 업무 보고 조회", description = "일일 업무 보고를 조회하는 API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "업무 보고 조회 성공"
                    ),
            }
    )
    ResponseEntity<ReportFindAllResponse> getReportsByAllUser(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String keyword
    );

    @Operation(summary = "일별 보고 엑셀파일 다운로드", description = "일별로 업무보고 엑셀파일을 다운로드할 수 있는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "파일 다운로드 성공"
                    ),
            }
    )
    ResponseEntity<InputStreamResource> createReportExcel(
            @RequestParam LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    );

    @Operation(summary = "일별 보고 워드파일 다운로드", description = "일별로 업무보고 엑셀파일을 다운로드할 수 있는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "파일 다운로드 성공"
                    ),
            }
    )
    ResponseEntity<InputStreamResource> getWordFileByCurrentUser(
            @RequestParam LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    );
}
