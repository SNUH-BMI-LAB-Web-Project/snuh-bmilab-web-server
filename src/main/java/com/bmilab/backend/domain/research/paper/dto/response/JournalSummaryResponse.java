package com.bmilab.backend.domain.research.paper.dto.response;

import com.bmilab.backend.domain.research.paper.enums.JournalCategory;
import io.swagger.v3.oas.annotations.media.Schema;

public record JournalSummaryResponse(
        @Schema(description = "저널 ID")
        Long id,

        @Schema(description = "저널명")
        String journalName,

        @Schema(description = "저널 분류")
        JournalCategory category,

        @Schema(description = "출판사")
        String publisher,

        @Schema(description = "ISSN")
        String issn,

        @Schema(description = "JIF (Journal Impact Factor)")
        String jif
) {
}
