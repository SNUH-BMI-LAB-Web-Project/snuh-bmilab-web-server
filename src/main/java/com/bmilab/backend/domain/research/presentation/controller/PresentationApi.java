package com.bmilab.backend.domain.research.presentation.controller;

import com.bmilab.backend.domain.research.presentation.dto.request.CreateAcademicPresentationRequest;
import com.bmilab.backend.domain.research.presentation.dto.request.UpdateAcademicPresentationRequest;
import com.bmilab.backend.domain.research.presentation.dto.response.AcademicPresentationResponse;
import com.bmilab.backend.domain.research.presentation.dto.response.AcademicPresentationSummaryResponse;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Research", description = "연구 실적 관리 API")
public interface PresentationApi {

    @Operation(summary = "학회발표 생성", description = "새로운 학회발표를 생성하는 POST API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "학회발표 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<AcademicPresentationResponse> createAcademicPresentation(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid CreateAcademicPresentationRequest request
    );

    @Operation(summary = "학회발표 수정", description = "학회발표를 수정하는 PUT API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "학회발표 수정 성공"),
            @ApiResponse(responseCode = "404", description = "학회발표를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<AcademicPresentationResponse> updateAcademicPresentation(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long academicPresentationId,
            @RequestBody @Valid UpdateAcademicPresentationRequest request
    );

    @Operation(summary = "학회발표 단건 조회", description = "학회발표 ID로 학회발표를 상세 조회하는 GET API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "학회발표 조회 성공"),
            @ApiResponse(responseCode = "404", description = "학회발표를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<AcademicPresentationResponse> getAcademicPresentation(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long academicPresentationId
    );

    @Operation(summary = "학회발표 목록 조회", description = "검색어와 함께 학회발표 목록을 조회하는 GET API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "학회발표 목록 조회 성공")
    })
    ResponseEntity<Page<AcademicPresentationSummaryResponse>> getAcademicPresentations(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable
    );

    @Operation(summary = "학회발표 삭제", description = "학회발표를 삭제하는 DELETE API (관리자 전용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "학회발표 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "학회발표를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Void> deleteAcademicPresentation(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long academicPresentationId
    );
}
