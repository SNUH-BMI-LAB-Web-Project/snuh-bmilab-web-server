package com.bmilab.backend.domain.research.award.dto.response;

import com.bmilab.backend.domain.research.award.entity.Award;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record AwardSummaryResponse(
        Long id,
        String awardName,
        String recipients,
        String competitionName,
        LocalDate awardDate
) {
    public AwardSummaryResponse(Award award) {
        this(
                award.getId(),
                award.getAwardName(),
                award.getRecipients(),
                award.getCompetitionName(),
                award.getAwardDate()
        );
    }
}
