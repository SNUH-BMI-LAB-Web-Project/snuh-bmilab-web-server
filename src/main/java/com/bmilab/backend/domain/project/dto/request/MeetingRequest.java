package com.bmilab.backend.domain.project.dto.request;

import com.bmilab.backend.domain.project.enums.MeetingType;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;

public record MeetingRequest(
        String title,
        LocalDate date,
        @DateTimeFormat(pattern = "HH:mm")
        LocalTime startTime,
        @DateTimeFormat(pattern = "HH:mm")
        LocalTime endTime,
        MeetingType type,
        String summary,
        String content
) {
}
