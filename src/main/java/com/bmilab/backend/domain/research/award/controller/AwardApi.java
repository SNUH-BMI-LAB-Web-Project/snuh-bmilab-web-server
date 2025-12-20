package com.bmilab.backend.domain.research.award.controller;

import com.bmilab.backend.domain.research.award.dto.request.CreateAwardRequest;
import com.bmilab.backend.domain.research.award.dto.request.UpdateAwardRequest;
import com.bmilab.backend.domain.research.award.dto.response.AwardResponse;
import com.bmilab.backend.domain.research.award.dto.response.AwardSummaryResponse;
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

@Tag(name = "Research", description = "연구 실적 관리 API ")
public interface AwardApi {

    @Operation(summary = "수상 생성", description = "새로운 수상을 생성하는 POST API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "수상 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<AwardResponse> createAward(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid CreateAwardRequest request
    );

    @Operation(summary = "수상 수정", description = "수상을 수정하는 PUT API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수상 수정 성공"),
            @ApiResponse(responseCode = "404", description = "수상을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<AwardResponse> updateAward(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long awardId,
            @RequestBody @Valid UpdateAwardRequest request
    );

    @Operation(summary = "수상 단건 조회", description = "수상 ID로 수상을 상세 조회하는 GET API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수상 조회 성공"),
            @ApiResponse(responseCode = "404", description = "수상을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<AwardResponse> getAward(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long awardId
    );

    @Operation(summary = "수상 목록 조회", description = "검색어와 함께 수상 목록을 조회하는 GET API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수상 목록 조회 성공")
    })
    ResponseEntity<Page<AwardSummaryResponse>> getAwards(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable
    );

    @Operation(summary = "수상 삭제", description = "수상을 삭제하는 DELETE API (관리자 전용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수상 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "수상을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Void> deleteAward(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long awardId
    );
}
