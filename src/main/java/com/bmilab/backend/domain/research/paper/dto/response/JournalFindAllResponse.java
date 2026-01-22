package com.bmilab.backend.domain.research.paper.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record JournalFindAllResponse(
        @Schema(description = "저널 목록")
        List<JournalSummaryResponse> journals,

        @Schema(description = "전체 페이지 수")
        int totalPage
) {
    public static JournalFindAllResponse of(List<JournalSummaryResponse> journals, int totalPage) {
        return JournalFindAllResponse.builder()
                .journals(journals)
                .totalPage(totalPage)
                .build();
    }
}
