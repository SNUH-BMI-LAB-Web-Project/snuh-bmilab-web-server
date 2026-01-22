package com.bmilab.backend.domain.research.patent.controller;

import com.bmilab.backend.domain.research.patent.dto.request.CreatePatentRequest;
import com.bmilab.backend.domain.research.patent.dto.request.UpdatePatentRequest;
import com.bmilab.backend.domain.research.patent.dto.response.PatentFindAllResponse;
import com.bmilab.backend.domain.research.patent.dto.response.PatentResponse;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Research", description = "연구 실적 관리 API")
public interface PatentApi {

    @Operation(summary = "특허 생성", description = "새로운 특허를 생성하는 POST API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "특허 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<PatentResponse> createPatent(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid CreatePatentRequest request
    );

    @Operation(summary = "특허 수정", description = "특허를 수정하는 PUT API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특허 수정 성공"),
            @ApiResponse(responseCode = "404", description = "특허를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<PatentResponse> updatePatent(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long patentId,
            @RequestBody @Valid UpdatePatentRequest request
    );

    @Operation(summary = "특허 단건 조회", description = "특허 ID로 특허를 상세 조회하는 GET API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특허 조회 성공"),
            @ApiResponse(responseCode = "404", description = "특허를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<PatentResponse> getPatent(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long patentId
    );

    @Operation(summary = "특허 목록 조회", description = "검색어와 함께 특허 목록을 조회하는 GET API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특허 목록 조회 성공")
    })
    ResponseEntity<PatentFindAllResponse> getPatents(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable
    );

    @Operation(summary = "특허 삭제", description = "특허를 삭제하는 DELETE API (관리자 전용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특허 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "특허를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Void> deletePatent(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long patentId
    );
}
