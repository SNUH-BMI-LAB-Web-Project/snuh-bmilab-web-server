package com.bmilab.backend.domain.seminar.dto.response;

import com.bmilab.backend.domain.seminar.entity.Seminar;
import com.bmilab.backend.domain.seminar.enums.SeminarLabel;
import io.swagger.v3.oas.annotations.media.Schema;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record SeminarResponse(
    @Schema(description = "세미나/학회 ID")
    Long id,

    @Schema(description = "라벨")
    SeminarLabel label,

    @Schema(description = "제목")
    String title,

    @Schema(description = "시작일")
    LocalDate startDate,

    @Schema(description = "종료일")
    LocalDate endDate,

    @Schema(description = "시작 시간")
    LocalTime startTime,

    @Schema(description = "종료 시간")
    LocalTime endTime,

    @Schema(description = "기타 메모")
    String note,

    @Schema(description = "등록자 ID")
    Long userId,

    @Schema(description = "등록자 이름")
    String userName,

    @Schema(description = "생성일시")
    LocalDateTime createdAt,

    @Schema(description = "Google 캘린더 추가 링크")
    String googleCalendarLink
) {
    public static SeminarResponse from(Seminar seminar) {
        String eventTitle = "[" + seminar.getLabel().getDescription() + "] " + seminar.getTitle();
        return new SeminarResponse(
            seminar.getId(),
            seminar.getLabel(),
            seminar.getTitle(),
            seminar.getStartDate(),
            seminar.getEndDate(),
            seminar.getStartTime(),
            seminar.getEndTime(),
            seminar.getNote(),
            seminar.getUser().getId(),
            seminar.getUser().getName(),
            seminar.getCreatedAt(),
            buildGoogleCalendarLink(eventTitle, seminar.getStartDate(), seminar.getEndDate(),
                    seminar.getStartTime(), seminar.getEndTime())
        );
    }

    private static String buildGoogleCalendarLink(String title, LocalDate startDate, LocalDate endDate,
                                                   LocalTime startTime, LocalTime endTime) {
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String dates;

        if (startTime != null) {
            // AIDEV-NOTE: 시간이 있는 이벤트 — yyyyMMdd'T'HHmmss 형식
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
            String start = startDate.atTime(startTime).format(dtf);
            LocalTime effectiveEndTime = endTime != null ? endTime : startTime.plusHours(1);
            LocalDate effectiveEndDate = endDate != null ? endDate : startDate;
            String end = effectiveEndDate.atTime(effectiveEndTime).format(dtf);
            dates = start + "/" + end;
        } else {
            // AIDEV-NOTE: 종일 이벤트 — yyyyMMdd 형식, endDate는 exclusive이므로 +1일
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
            String start = startDate.format(df);
            LocalDate effectiveEnd = (endDate != null ? endDate : startDate).plusDays(1);
            String end = effectiveEnd.format(df);
            dates = start + "/" + end;
        }

        return "https://calendar.google.com/calendar/render?action=TEMPLATE"
                + "&text=" + encodedTitle
                + "&dates=" + dates;
    }
}
