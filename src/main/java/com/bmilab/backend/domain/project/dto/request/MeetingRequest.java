package com.bmilab.backend.domain.project.dto.request;

import com.bmilab.backend.domain.project.enums.MeetingType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;

public record MeetingRequest(
        String title,
        LocalDate date,
        @Schema(type = "string", pattern = "HH:mm", example = "14:30")
        LocalTime startTime,
        @Schema(type = "string", pattern = "HH:mm", example = "14:30")
        LocalTime endTime,
        MeetingType type,
        String summary,
        String content
) {
}
