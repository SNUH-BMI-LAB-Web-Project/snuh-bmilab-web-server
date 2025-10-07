package com.bmilab.backend.domain.task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record TaskAgreementUpdateRequest(
        @Schema(description = "협약 체결일", example = "2025-10-01")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate agreementDate
) {
}
