package com.bmilab.backend.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "(Admin) Comment", description = "(관리자용) 댓글 API")
public interface AdminCommentApi {
    @Operation(summary = "(관리자) 댓글 삭제", description = "관리자가 임의로 댓글을 삭제하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "댓글 삭제 성공"
                    )
            }
    )
    ResponseEntity<Void> deleteComment(@PathVariable Long commentId);
}
