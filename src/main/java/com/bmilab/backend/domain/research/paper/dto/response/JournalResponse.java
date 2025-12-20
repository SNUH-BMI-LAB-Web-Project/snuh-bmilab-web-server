package com.bmilab.backend.domain.research.paper.dto.response;

import com.bmilab.backend.domain.research.paper.entity.Journal;
import com.bmilab.backend.domain.research.paper.enums.JournalCategory;
import io.swagger.v3.oas.annotations.media.Schema;

public record JournalResponse(
        @Schema(description = "저널 ID")
        Long id,

        @Schema(description = "저널명")
        String journalName,

        @Schema(description = "저널 분류")
        JournalCategory category,

        @Schema(description = "출판사")
        String publisher,

        @Schema(description = "출판 국가")
        String publishCountry,

        @Schema(description = "ISBN")
        String isbn,

        @Schema(description = "ISSN")
        String issn,

        @Schema(description = "eISSN")
        String eissn,

        @Schema(description = "JIF (Journal Impact Factor)")
        String jif,

        @Schema(description = "JCR Rank")
        String jcrRank
) {
    public static JournalResponse from(Journal journal) {
        return new JournalResponse(
                journal.getId(),
                journal.getJournalName(),
                journal.getCategory(),
                journal.getPublisher(),
                journal.getPublishCountry(),
                journal.getIsbn(),
                journal.getIssn(),
                journal.getEissn(),
                journal.getJif(),
                journal.getJcrRank()
        );
    }
}
