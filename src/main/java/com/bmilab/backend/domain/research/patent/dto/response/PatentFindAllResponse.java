package com.bmilab.backend.domain.research.patent.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record PatentFindAllResponse(
        @Schema(description = "특허 목록")
        List<PatentSummaryResponse> patents,

        @Schema(description = "전체 페이지 수", example = "5")
        int totalPage
) {
    public static PatentFindAllResponse of(List<PatentSummaryResponse> patents, int totalPage) {
        return PatentFindAllResponse.builder()
                .patents(patents)
                .totalPage(totalPage)
                .build();
    }
}
