package com.bmilab.backend.domain.seminar.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record CreateSeminarResponse(
    @Schema(description = "생성된 세미나 ID 목록")
    List<Long> seminarIds
) {
    public static CreateSeminarResponse from(List<Long> ids) {
        return new CreateSeminarResponse(ids);
    }
}
