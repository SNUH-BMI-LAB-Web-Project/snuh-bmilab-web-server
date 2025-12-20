package com.bmilab.backend.domain.research.controller;

import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "Research", description = "연구 실적 관리 API")
public interface ResearchExcelApi {

    @Operation(summary = "논문 Excel 다운로드", description = "모든 논문 데이터를 Excel 파일로 다운로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Excel 파일 다운로드 성공")
    })
    ResponseEntity<byte[]> downloadPapersExcel(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo
    );

    @Operation(summary = "특허 Excel 다운로드", description = "모든 특허 데이터를 Excel 파일로 다운로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Excel 파일 다운로드 성공")
    })
    ResponseEntity<byte[]> downloadPatentsExcel(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo
    );

    @Operation(summary = "저서 Excel 다운로드", description = "모든 저서 데이터를 Excel 파일로 다운로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Excel 파일 다운로드 성공")
    })
    ResponseEntity<byte[]> downloadPublicationsExcel(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo
    );

    @Operation(summary = "학회발표 Excel 다운로드", description = "모든 학회발표 데이터를 Excel 파일로 다운로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Excel 파일 다운로드 성공")
    })
    ResponseEntity<byte[]> downloadAcademicPresentationsExcel(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo
    );

    @Operation(summary = "수상 Excel 다운로드", description = "모든 수상 데이터를 Excel 파일로 다운로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Excel 파일 다운로드 성공")
    })
    ResponseEntity<byte[]> downloadAwardsExcel(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo
    );
}
