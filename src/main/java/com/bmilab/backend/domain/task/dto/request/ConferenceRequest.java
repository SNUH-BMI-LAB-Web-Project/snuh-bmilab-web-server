package com.bmilab.backend.domain.task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ConferenceRequest(
        @Schema(description = "발표 제목", example = "Deep Learning for Medical Image Analysis")
        @NotBlank(message = "발표 제목은 필수입니다.")
        String presentationTitle,

        @Schema(description = "학회명", example = "MICCAI 2024")
        @NotBlank(message = "학회명은 필수입니다.")
        String conferenceName,

        @Schema(description = "발표자", example = "홍길동")
        @NotBlank(message = "발표자는 필수입니다.")
        String presenter,

        @Schema(description = "발표일", example = "2025-10-20")
        LocalDate presentationDate
) {
}
