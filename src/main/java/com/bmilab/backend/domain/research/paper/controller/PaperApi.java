package com.bmilab.backend.domain.research.paper.controller;

import com.bmilab.backend.domain.research.paper.dto.request.CreatePaperRequest;
import com.bmilab.backend.domain.research.paper.dto.request.UpdatePaperRequest;
import com.bmilab.backend.domain.research.paper.dto.response.PaperFindAllResponse;
import com.bmilab.backend.domain.research.paper.dto.response.PaperResponse;
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
public interface PaperApi {

    @Operation(summary = "논문 생성", description = "새로운 논문을 생성하는 POST API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "논문 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "저널을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<PaperResponse> createPaper(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid CreatePaperRequest request
    );

    @Operation(summary = "논문 수정", description = "논문을 수정하는 PUT API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "논문 수정 성공"),
            @ApiResponse(responseCode = "404", description = "논문을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<PaperResponse> updatePaper(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long paperId,
            @RequestBody @Valid UpdatePaperRequest request
    );

    @Operation(summary = "논문 단건 조회", description = "논문 ID로 논문을 상세 조회하는 GET API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "논문 조회 성공"),
            @ApiResponse(responseCode = "404", description = "논문을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<PaperResponse> getPaper(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long paperId
    );

    @Operation(summary = "논문 목록 조회", description = "검색어와 함께 논문 목록을 조회하는 GET API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "논문 목록 조회 성공")
    })
    ResponseEntity<PaperFindAllResponse> getPapers(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable
    );

    @Operation(summary = "논문 삭제", description = "논문을 삭제하는 DELETE API (관리자 전용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "논문 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "논문을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Void> deletePaper(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long paperId
    );
}
