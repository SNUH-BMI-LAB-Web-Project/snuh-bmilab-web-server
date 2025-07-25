package com.bmilab.backend.domain.comment.controller;

import com.bmilab.backend.domain.comment.dto.request.CommentRequest;
import com.bmilab.backend.domain.comment.dto.response.CommentFindAllResponse;
import com.bmilab.backend.domain.comment.enums.CommentDomainType;
import com.bmilab.backend.domain.comment.service.CommentService;
import com.bmilab.backend.global.security.UserAuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController implements CommentApi {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody CommentRequest request
    ) {

        commentService.createComment(userAuthInfo.getUserId(), request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long commentId,
            @RequestBody CommentRequest request
    ) {

        commentService.updateComment(userAuthInfo.getUserId(), commentId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<CommentFindAllResponse> getAllComments(
            @RequestParam CommentDomainType domainType,
            @RequestParam Long entityId
    ) {

        return ResponseEntity.ok(commentService.getAllComments(domainType, entityId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long commentId
    ) {
        commentService.deleteCommentByUser(userAuthInfo.getUserId(), commentId);
        return ResponseEntity.ok().build();
    }
}
