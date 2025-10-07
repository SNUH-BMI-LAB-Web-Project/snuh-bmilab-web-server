package com.bmilab.backend.domain.task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record PatentRequest(
        @Schema(description = "특허명", example = "의료 영상 분석 장치 및 방법")
        @NotBlank(message = "특허명은 필수입니다.")
        String patentTitle,

        @Schema(description = "특허 번호", example = "10-2024-0001234")
        String patentNumber,

        @Schema(description = "출원일", example = "2025-10-15")
        LocalDate applicationDate
) {
}
