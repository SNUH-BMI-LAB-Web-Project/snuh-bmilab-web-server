package com.bmilab.backend.domain.board.controller;

import com.bmilab.backend.domain.board.dto.request.BoardCategoryReqeust;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "(Admin) Board Category", description = "(관리자용) 게시판 분야 API")
public interface AdminBoardCategoryApi {

    @Operation(summary = "게시판 분야 추가", description = "새로운 게시판 분야를 추가하기 위한 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "게시판 분야 추가 성공"
                    ),
            }
    )
    ResponseEntity<Void> createBoardCategory(@RequestBody @Valid BoardCategoryReqeust request);

    @Operation(summary = "게시판 분야 수정", description = "기존 게시판 분야를 수정하기 위한 PUT API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "전체 게시판 분야 조회 성공"
                    ),
            }
    )
    ResponseEntity<Void> updateBoardCategory(
            @PathVariable Long categoryId,
            @RequestBody @Valid BoardCategoryReqeust request
    );

    @Operation(summary = "게시판 분야 삭제", description = "게시판 분야를 삭제하기 위한 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "게시판 분야 삭제 성공"
                    ),
            }
    )
    ResponseEntity<Void> deleteBoardCategory(@PathVariable Long categoryId);
}
