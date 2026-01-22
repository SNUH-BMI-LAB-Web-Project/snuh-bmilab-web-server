package com.bmilab.backend.domain.research.publication.controller;

import com.bmilab.backend.domain.research.publication.dto.request.CreateAuthorRequest;
import com.bmilab.backend.domain.research.publication.dto.request.UpdateAuthorRequest;
import com.bmilab.backend.domain.research.publication.dto.response.AuthorFindAllResponse;
import com.bmilab.backend.domain.research.publication.dto.response.AuthorResponse;
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
public interface PublicationApi {

    @Operation(summary = "저서 생성", description = "새로운 저서를 생성하는 POST API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "저서 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<AuthorResponse> createPublication(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid CreateAuthorRequest request
    );

    @Operation(summary = "저서 수정", description = "저서를 수정하는 PUT API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저서 수정 성공"),
            @ApiResponse(responseCode = "404", description = "저서를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<AuthorResponse> updatePublication(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long authorId,
            @RequestBody @Valid UpdateAuthorRequest request
    );

    @Operation(summary = "저서 단건 조회", description = "저서 ID로 저서를 상세 조회하는 GET API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저서 조회 성공"),
            @ApiResponse(responseCode = "404", description = "저서를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<AuthorResponse> getPublication(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long authorId
    );

    @Operation(summary = "저서 목록 조회", description = "검색어와 함께 저서 목록을 조회하는 GET API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저서 목록 조회 성공")
    })
    ResponseEntity<AuthorFindAllResponse> getPublications(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable
    );

    @Operation(summary = "저서 삭제", description = "저서를 삭제하는 DELETE API (관리자 전용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저서 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "저서를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    ResponseEntity<Void> deletePublication(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long authorId
    );
}
