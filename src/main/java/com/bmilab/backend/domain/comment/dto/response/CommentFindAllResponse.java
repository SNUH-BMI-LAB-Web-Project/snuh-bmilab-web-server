package com.bmilab.backend.domain.comment.dto.response;

import com.bmilab.backend.domain.comment.entity.Comment;
import com.bmilab.backend.domain.comment.enums.CommentDomainType;
import com.bmilab.backend.domain.user.dto.response.UserSummary;
import io.swagger.v3.oas.annotations.media.Schema;
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
            @Schema(description = "댓글 ID", example = "1001")
            Long commentId,

            @Schema(description = "작성자 정보")
            UserSummary user,

            @Schema(description = "댓글 내용", example = "좋은 글 감사합니다!")
            String message,

            @Schema(description = "댓글 도메인 타입")
            CommentDomainType domainType,

            @Schema(description = "댓글이 속한 엔티티 ID", example = "42")
            Long entityId,

            @Schema(description = "댓글 작성 시간")
            LocalDateTime createdAt,

            @Schema(description = "댓글 수정 시간")
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