package com.bmilab.backend.global.external.calendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@Service
public class GoogleCalendarService {

    private static final String TIMEZONE = "Asia/Seoul";

    private final Calendar googleCalendar;

    @Autowired
    public GoogleCalendarService(@Autowired(required = false) Calendar googleCalendar) {
        this.googleCalendar = googleCalendar;
    }

    public boolean isEnabled() {
        return googleCalendar != null;
    }

    public String createEvent(String calendarId, String title, LocalDate startDate, LocalDate endDate) {
        return createEvent(calendarId, title, startDate, endDate, null, null);
    }

    public String createEvent(String calendarId, String title, LocalDate startDate, LocalDate endDate,
                               LocalTime startTime, LocalTime endTime) {
        if (!isEnabled()) return null;
        try {
            Event event = buildEvent(title, startDate, endDate, startTime, endTime);
            Event created = googleCalendar.events().insert(calendarId, event).execute();
            log.info("Google Calendar 이벤트 생성 완료: calendarId={}, eventId={}", calendarId, created.getId());
            return created.getId();
        } catch (Exception e) {
            log.error("Google Calendar 이벤트 생성 실패: calendarId={}, title={}", calendarId, title, e);
            return null;
        }
    }

    public String updateEvent(String calendarId, String eventId, String title, LocalDate startDate, LocalDate endDate) {
        return updateEvent(calendarId, eventId, title, startDate, endDate, null, null);
    }

    public String updateEvent(String calendarId, String eventId, String title, LocalDate startDate, LocalDate endDate,
                               LocalTime startTime, LocalTime endTime) {
        if (!isEnabled()) return null;
        try {
            Event event = buildEvent(title, startDate, endDate, startTime, endTime);
            Event updated = googleCalendar.events().update(calendarId, eventId, event).execute();
            log.info("Google Calendar 이벤트 수정 완료: calendarId={}, eventId={}", calendarId, updated.getId());
            return updated.getId();
        } catch (Exception e) {
            log.error("Google Calendar 이벤트 수정 실패: calendarId={}, eventId={}", calendarId, eventId, e);
            return null;
        }
    }

    public void deleteEvent(String calendarId, String eventId) {
        if (!isEnabled()) return;
        try {
            googleCalendar.events().delete(calendarId, eventId).execute();
            log.info("Google Calendar 이벤트 삭제 완료: calendarId={}, eventId={}", calendarId, eventId);
        } catch (Exception e) {
            log.error("Google Calendar 이벤트 삭제 실패: calendarId={}, eventId={}", calendarId, eventId, e);
        }
    }

    // AIDEV-NOTE: startTime이 있으면 시간 지정 이벤트, 없으면 종일 이벤트로 생성
    private Event buildEvent(String title, LocalDate startDate, LocalDate endDate,
                              LocalTime startTime, LocalTime endTime) {
        Event event = new Event().setSummary(title);

        if (startTime != null) {
            ZonedDateTime startZdt = startDate.atTime(startTime).atZone(ZoneId.of(TIMEZONE));
            event.setStart(new EventDateTime()
                    .setDateTime(new DateTime(startZdt.toInstant().toEpochMilli()))
                    .setTimeZone(TIMEZONE));

            LocalTime effectiveEndTime = endTime != null ? endTime : startTime.plusHours(1);
            LocalDate effectiveEndDate = endDate != null ? endDate : startDate;
            ZonedDateTime endZdt = effectiveEndDate.atTime(effectiveEndTime).atZone(ZoneId.of(TIMEZONE));
            event.setEnd(new EventDateTime()
                    .setDateTime(new DateTime(endZdt.toInstant().toEpochMilli()))
                    .setTimeZone(TIMEZONE));
        } else {
            // AIDEV-NOTE: Google Calendar all-day 이벤트의 endDate는 exclusive이므로 +1일 처리
            event.setStart(new EventDateTime()
                    .setDate(new DateTime(startDate.toString())));

            LocalDate effectiveEnd = (endDate != null ? endDate : startDate).plusDays(1);
            event.setEnd(new EventDateTime()
                    .setDate(new DateTime(effectiveEnd.toString())));
        }

        return event;
    }
}
