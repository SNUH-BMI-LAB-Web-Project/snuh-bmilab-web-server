package com.bmilab.backend.domain.seminar.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record SeminarFindAllResponse(
    @Schema(description = "세미나/학회 목록")
    List<SeminarResponse> seminars,

    @Schema(description = "전체 페이지 수")
    Integer totalPages
) {
    public static SeminarFindAllResponse of(List<SeminarResponse> seminars) {
        return new SeminarFindAllResponse(seminars, null);
    }

    public static SeminarFindAllResponse of(List<SeminarResponse> seminars, int totalPages) {
        return new SeminarFindAllResponse(seminars, totalPages);
    }
}
