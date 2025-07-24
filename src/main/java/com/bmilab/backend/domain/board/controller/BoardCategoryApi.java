package com.bmilab.backend.domain.board.controller;

import com.bmilab.backend.domain.board.dto.response.BoardCategoryFindAllResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Board Category", description = "게시판 분야 API")
public interface BoardCategoryApi {
    @Operation(summary = "전체 게시판 분야 조회", description = "전체 게시글 분야 목록을 조회하기 위한 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "전체 게시판 분야 조회 성공"
                    ),
            }
    )
    ResponseEntity<BoardCategoryFindAllResponse> getAllBoardCategories();
    }
