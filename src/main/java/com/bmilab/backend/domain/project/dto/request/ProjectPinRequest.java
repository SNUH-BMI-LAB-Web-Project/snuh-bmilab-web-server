package com.bmilab.backend.domain.project.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ProjectPinRequest(
        @Schema(description = "연구 고정 여부", example = "true")
        @NotNull(message = "연구 고정 여부는 필수입니다.")
        boolean isPinned
) {
}
