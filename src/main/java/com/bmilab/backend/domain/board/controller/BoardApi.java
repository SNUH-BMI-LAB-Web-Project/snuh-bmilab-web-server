package com.bmilab.backend.domain.board.controller;

import com.bmilab.backend.domain.board.dto.request.BoardRequest;
import com.bmilab.backend.domain.board.dto.response.BoardDetail;
import com.bmilab.backend.domain.board.dto.response.BoardFindAllResponse;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "Board", description = "게시판 API")
public interface BoardApi {
    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "게시글 작성 성공"
                    )
            }
    )
   ResponseEntity<Void> createBoard(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody BoardRequest request
    );


    @Operation(summary = "게시글 수정", description = "게시글을 수정하는 PUT API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "게시글 수정 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "게시글 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "게시글에 접근할 수 있는 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> updateBoard(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long boardId,
            @RequestBody BoardRequest request
    );

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "게시글 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "게시글 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "게시글에 접근할 수 있는 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> deleteBoard(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long boardId
    );

    @Operation(summary = "게시글 첨부파일 삭제", description = "게시글에서 첨부파일을 삭제하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "첨부파일 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "게시글 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "게시글에 접근할 수 있는 권한이 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> deleteBoardFile(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long boardId,
            @PathVariable UUID fileId
    );

    @Operation(summary = "모든 게시글 조회", description = "모든 게시글을 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "모든 게시글 조회 성공"
                    )
            }
    )
    ResponseEntity<BoardFindAllResponse> getAllBoards(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable
    );

    @Operation(summary = "게시글 상세 조회", description = "ID로 게시글을 상세 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "게시글 상세 조회 성공"
                    )
            }
    )
    ResponseEntity<BoardDetail> getBoardById(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long boardId
    );
}
