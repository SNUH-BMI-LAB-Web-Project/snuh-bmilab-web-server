package com.bmilab.backend.domain.research.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record AcademicPresentationFindAllResponse(
        @Schema(description = "학회발표 목록")
        List<AcademicPresentationSummaryResponse> presentations,

        @Schema(description = "전체 페이지 수", example = "5")
        int totalPage
) {
    public static AcademicPresentationFindAllResponse of(List<AcademicPresentationSummaryResponse> presentations, int totalPage) {
        return AcademicPresentationFindAllResponse.builder()
                .presentations(presentations)
                .totalPage(totalPage)
                .build();
    }
}
