package com.bmilab.backend.domain.comment.controller;

import com.bmilab.backend.domain.comment.dto.request.CommentRequest;
import com.bmilab.backend.domain.comment.dto.response.CommentFindAllResponse;
import com.bmilab.backend.domain.comment.enums.CommentDomainType;
import com.bmilab.backend.global.security.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Comment", description = "댓글 API")
public interface CommentApi {
    @Operation(summary = "댓글 생성", description = "댓글을 생성하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "댓글 생성 성공"
                    )
            }
    )
    ResponseEntity<Void> createComment(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody CommentRequest request
    );

    @Operation(summary = "댓글 수정", description = "댓글을 수정하는 PUT API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "댓글 수정 성공"
                    )
            }
    )
    ResponseEntity<Void> updateComment(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long commentId,
            @RequestBody CommentRequest request
    );

    @Operation(summary = "댓글 목록 조회", description = "댓글 목록을 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "댓글 목록 조회 성공"
                    )
            }
    )
    ResponseEntity<CommentFindAllResponse> getAllComments(
            @RequestParam CommentDomainType domainType,
            @RequestParam Long entityId
    );

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "댓글 삭제 성공"
                    )
            }
    )
    ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long commentId
    );
}
