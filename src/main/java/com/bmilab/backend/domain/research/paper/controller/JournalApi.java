package com.bmilab.backend.domain.research.paper.controller;

import com.bmilab.backend.domain.research.paper.dto.request.CreateJournalRequest;
import com.bmilab.backend.domain.research.paper.dto.request.UpdateJournalRequest;
import com.bmilab.backend.domain.research.paper.dto.response.JournalFindAllResponse;
import com.bmilab.backend.domain.research.paper.dto.response.JournalResponse;
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
public interface JournalApi {

    @Operation(summary = "저널 생성", description = "새로운 저널을 생성하는 POST API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "저널 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<JournalResponse> createJournal(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid CreateJournalRequest request
    );

    @Operation(summary = "저널 수정", description = "저널을 수정하는 PUT API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저널 수정 성공"),
            @ApiResponse(responseCode = "404", description = "저널을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<JournalResponse> updateJournal(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long journalId,
            @RequestBody @Valid UpdateJournalRequest request
    );

    @Operation(summary = "저널 단건 조회", description = "저널 ID로 저널을 상세 조회하는 GET API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저널 조회 성공"),
            @ApiResponse(responseCode = "404", description = "저널을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<JournalResponse> getJournal(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long journalId
    );

    @Operation(summary = "저널 목록 조회", description = "검색어(저널명, 출판사, ISSN)와 함께 저널 목록을 조회하는 GET API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저널 목록 조회 성공")
    })
    ResponseEntity<JournalFindAllResponse> getJournals(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable
    );

    @Operation(summary = "저널 삭제", description = "저널을 삭제하는 DELETE API (관리자 전용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저널 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "저널을 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Void> deleteJournal(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long journalId
    );
}
