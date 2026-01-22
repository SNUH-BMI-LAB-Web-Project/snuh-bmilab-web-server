package com.bmilab.backend.domain.research.award.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record AwardFindAllResponse(
        @Schema(description = "수상 목록")
        List<AwardSummaryResponse> awards,

        @Schema(description = "전체 페이지 수", example = "5")
        int totalPage
) {
    public static AwardFindAllResponse of(List<AwardSummaryResponse> awards, int totalPage) {
        return AwardFindAllResponse.builder()
                .awards(awards)
                .totalPage(totalPage)
                .build();
    }
}
