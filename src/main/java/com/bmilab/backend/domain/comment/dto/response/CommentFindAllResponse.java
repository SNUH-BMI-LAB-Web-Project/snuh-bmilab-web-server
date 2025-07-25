package com.bmilab.backend.domain.comment.dto.response;

import com.bmilab.backend.domain.comment.entity.Comment;
import com.bmilab.backend.domain.comment.enums.CommentDomainType;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record CommentFindAllResponse(
        List<CommentSummary> comments
) {
    public static CommentFindAllResponse of(List<Comment> comments) {
        return new CommentFindAllResponse(
                comments.stream()
                        .map(CommentSummary::from)
                        .toList()
        );
    }

    @Builder
    public record CommentSummary(
            Long commentId,
            UserSummary user,
            String message,
            CommentDomainType domainType,
            Long entityId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {

        public static CommentSummary from(Comment comment) {

            return CommentSummary.builder()
                    .commentId(comment.getId())
                    .user(UserSummary.from(comment.getUser()))
                    .message(comment.getMessage())
                    .domainType(comment.getDomainType())
                    .entityId(comment.getEntityId())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .build();
        }
    }
}
