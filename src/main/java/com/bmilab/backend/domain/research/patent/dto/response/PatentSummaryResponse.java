package com.bmilab.backend.domain.research.patent.dto.response;

import com.bmilab.backend.domain.research.patent.entity.Patent;

import java.time.LocalDate;

public record PatentSummaryResponse(
        Long id,
        String patentName,
        String applicationNumber,
        String applicantsAll,
        LocalDate applicationDate
) {
    public PatentSummaryResponse(Patent patent) {
        this(
                patent.getId(),
                patent.getPatentName(),
                patent.getApplicationNumber(),
                patent.getApplicantsAll(),
                patent.getApplicationDate()
        );
    }
}
