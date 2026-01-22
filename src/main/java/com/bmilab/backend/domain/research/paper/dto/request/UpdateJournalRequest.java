package com.bmilab.backend.domain.research.paper.dto.request;

import com.bmilab.backend.domain.research.paper.enums.JournalCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateJournalRequest(
        @Schema(description = "저널명")
        @NotBlank(message = "저널명은 필수입니다.")
        String journalName,

        @Schema(description = "저널 분류", example = "SCI")
        @NotNull(message = "저널 분류는 필수입니다.")
        JournalCategory category,

        @Schema(description = "출판사")
        @NotBlank(message = "출판사는 필수입니다.")
        String publisher,

        @Schema(description = "출판 국가")
        @NotBlank(message = "출판 국가는 필수입니다.")
        String publishCountry,

        @Schema(description = "ISBN")
        String isbn,

        @Schema(description = "ISSN", example = "1367-4811")
        @NotBlank(message = "ISSN은 필수입니다.")
        String issn,

        @Schema(description = "eISSN")
        String eissn,

        @Schema(description = "JIF (Journal Impact Factor)")
        @NotBlank(message = "JIF는 필수입니다.")
        String jif,

        @Schema(description = "JCR Rank")
        @NotBlank(message = "JCR Rank는 필수입니다.")
        String jcrRank,

        @Schema(description = "Issue")
        String issue
) {
}
