package com.bmilab.backend.domain.comment.service;

import com.bmilab.backend.domain.comment.dto.request.CommentRequest;
import com.bmilab.backend.domain.comment.dto.response.CommentFindAllResponse;
import com.bmilab.backend.domain.comment.entity.Comment;
import com.bmilab.backend.domain.comment.enums.CommentDomainType;
import com.bmilab.backend.domain.comment.exception.CommentErrorCode;
import com.bmilab.backend.domain.comment.repository.CommentRepository;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.service.UserService;
import com.bmilab.backend.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(CommentErrorCode.COMMENT_NOT_FOUND));
    }

    @Transactional
    public void createComment(Long userId, CommentRequest request) {
        User user = userService.findUserById(userId);

        Comment comment = Comment.builder()
                .user(user)
                .message(request.message())
                .domainType(request.domainType())
                .entityId(request.entityId())
                .build();

        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long userId, Long commentId, CommentRequest request) {

        User user = userService.findUserById(userId);
        Comment comment = findCommentById(commentId);

        validateUserIsCommentAuthor(user, comment);

        comment.updateMessage(request.message());
    }

    private void validateUserIsCommentAuthor(User user, Comment comment) {
        if (!comment.isAuthor(user)) {
            throw new ApiException(CommentErrorCode.COMMENT_ACCESS_DENIED);
        }
    }

    public CommentFindAllResponse getAllComments(CommentDomainType domainType, Long entityId) {

        List<Comment> comments = commentRepository.findAllByDomainTypeAndEntityId(
                domainType,
                entityId
        );

        return CommentFindAllResponse.of(comments);
    }

    @Transactional
    public void deleteCommentByUser(Long userId, Long commentId) {

        User user = userService.findUserById(userId);
        Comment comment = findCommentById(commentId);

        validateUserIsCommentAuthor(user, comment);

        commentRepository.delete(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = findCommentById(commentId);

        commentRepository.delete(comment);
    }
}
