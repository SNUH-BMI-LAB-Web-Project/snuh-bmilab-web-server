package com.bmilab.backend.domain.board.controller;

import com.bmilab.backend.domain.board.dto.request.BoardPinRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "(Admin) Board", description = "(관리자용) 게시판 API")
public interface AdminBoardApi {

    @Operation(summary = "게시글 고정 상태 수정", description = "기존 게시글 고정상태를 수정하기 위한 PATCH API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 고정 상태 수정 성공"),
    })
    ResponseEntity<Void> updateBoardPinStatus(
            @PathVariable Long boardId,
            @RequestBody BoardPinRequest request
    );
}