package com.bmilab.backend.domain.research.paper.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record PaperFindAllResponse(
        @Schema(description = "논문 목록")
        List<PaperSummaryResponse> papers,

        @Schema(description = "전체 페이지 수", example = "5")
        int totalPage
) {
    public static PaperFindAllResponse of(List<PaperSummaryResponse> papers, int totalPage) {
        return PaperFindAllResponse.builder()
                .papers(papers)
                .totalPage(totalPage)
                .build();
    }
}
