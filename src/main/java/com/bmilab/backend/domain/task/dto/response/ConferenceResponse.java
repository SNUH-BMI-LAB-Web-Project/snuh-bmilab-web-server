package com.bmilab.backend.domain.task.dto.response;

import com.bmilab.backend.domain.task.entity.Conference;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ConferenceResponse(
        @Schema(description = "학회 발표 ID")
        Long id,

        @Schema(description = "발표 제목")
        String presentationTitle,

        @Schema(description = "학회명")
        String conferenceName,

        @Schema(description = "발표자")
        String presenter,

        @Schema(description = "발표일")
        LocalDate presentationDate
) {
    public static ConferenceResponse from(Conference conference) {
        return new ConferenceResponse(
                conference.getId(),
                conference.getPresentationTitle(),
                conference.getConferenceName(),
                conference.getPresenter(),
                conference.getPresentationDate()
        );
    }
}
