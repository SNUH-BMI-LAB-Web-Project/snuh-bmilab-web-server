package com.bmilab.backend.domain.research.paper.dto.response;

import com.bmilab.backend.domain.research.paper.entity.Paper;

import java.time.LocalDate;

public record PaperSummaryResponse(
        Long id,
        String paperTitle,
        String journalName,
        String allAuthors,
        LocalDate acceptDate,
        LocalDate publishDate
) {
    public PaperSummaryResponse(Paper paper) {
        this(
                paper.getId(),
                paper.getPaperTitle(),
                paper.getJournal().getJournalName(),
                paper.getAllAuthors(),
                paper.getAcceptDate(),
                paper.getPublishDate()
        );
    }
}
