package com.bmilab.backend.domain.comment.controller;

import com.bmilab.backend.domain.comment.service.CommentService;
import com.bmilab.backend.global.annotation.OnlyAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@OnlyAdmin
@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController implements AdminCommentApi {

    private final CommentService commentService;

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {

        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
