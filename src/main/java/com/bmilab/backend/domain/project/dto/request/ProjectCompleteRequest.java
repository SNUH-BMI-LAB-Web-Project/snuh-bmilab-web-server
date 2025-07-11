package com.bmilab.backend.domain.project.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record ProjectCompleteRequest(
        @Schema(description = "연구 종료 날짜", example = "2025-04-25")
        @NotNull(message = "연구 종료 날짜는 필수입니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate
) {
}
