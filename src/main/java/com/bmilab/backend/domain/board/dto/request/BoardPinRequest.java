package com.bmilab.backend.domain.board.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record BoardPinRequest(

        @Schema(description = "게시글 고정 여부", example = "true")
        @NotNull(message = "게시글 고정 여부는 필수입니다.")
        boolean isPinned,

        @Schema(description = "게시글 버전", example = "1")
        @NotNull(message = "게시글 버전은 필수입니다.")
        Long version
) {
}
