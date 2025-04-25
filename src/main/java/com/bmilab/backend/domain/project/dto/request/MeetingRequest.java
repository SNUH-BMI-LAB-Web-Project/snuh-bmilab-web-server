package com.bmilab.backend.domain.project.dto.request;

import com.bmilab.backend.domain.project.enums.MeetingType;
import java.time.LocalDate;
import java.time.LocalTime;

public record MeetingRequest(
        String title,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        MeetingType type,
        String summary,
        String content
) {
}
