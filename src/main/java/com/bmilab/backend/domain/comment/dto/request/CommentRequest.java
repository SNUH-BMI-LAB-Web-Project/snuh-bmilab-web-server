package com.bmilab.backend.domain.comment.dto.request;

import com.bmilab.backend.domain.comment.enums.CommentDomainType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(
        @Schema(description = "댓글 내용", example = "댓글 메시지 예시입니다.")
        @NotBlank(message = "댓글 내용은 필수입니다.")
        String message,

        @Schema(description = "댓글 도메인 타입")
        @NotNull(message = "댓글 도메인 타입은 필수입니다.")
        CommentDomainType domainType,

        @Schema(description = "댓글 엔티티 ID", example = "1")
        @NotNull(message = "엔티티 ID는 필수입니다.")
        Long entityId
) {
}
