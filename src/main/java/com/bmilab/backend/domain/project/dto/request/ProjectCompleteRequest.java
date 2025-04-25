package com.bmilab.backend.domain.project.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record ProjectCompleteRequest(
        @Schema(description = "프로젝트 종료 날짜", example = "2025-04-25")
        LocalDate endDate
) {
}
