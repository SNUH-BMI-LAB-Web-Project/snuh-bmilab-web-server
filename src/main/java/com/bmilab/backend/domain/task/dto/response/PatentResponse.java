package com.bmilab.backend.domain.task.dto.response;

import com.bmilab.backend.domain.task.entity.Patent;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record PatentResponse(
        @Schema(description = "특허 ID")
        Long id,

        @Schema(description = "특허명")
        String patentTitle,

        @Schema(description = "특허 번호")
        String patentNumber,

        @Schema(description = "출원일")
        LocalDate applicationDate
) {
    public static PatentResponse from(Patent patent) {
        return new PatentResponse(
                patent.getId(),
                patent.getPatentTitle(),
                patent.getPatentNumber(),
                patent.getApplicationDate()
        );
    }
}
