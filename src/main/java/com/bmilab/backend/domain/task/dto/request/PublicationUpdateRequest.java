package com.bmilab.backend.domain.task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record PublicationUpdateRequest(
        @Schema(description = "논문 제목", example = "AI 기반 헬스케어 시스템 연구")
        @NotBlank(message = "논문 제목은 필수입니다.")
        String title,

        @Schema(description = "저자", example = "김연구, 박연구")
        @NotBlank(message = "저자는 필수입니다.")
        String authors,

        @Schema(description = "학술지명", example = "HealthCare")
        @NotBlank(message = "학술지명은 필수입니다.")
        String journal,

        @Schema(description = "게재일", example = "2025-11-15")
        LocalDate publicationDate,

        @Schema(description = "DOI", example = "10.1038/s41586-021-03819-2")
        String doi
) {
}