package com.bmilab.backend.global.external.calendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class GoogleCalendarService {

    private final Calendar googleCalendar;

    @Autowired
    public GoogleCalendarService(@Autowired(required = false) Calendar googleCalendar) {
        this.googleCalendar = googleCalendar;
    }

    public boolean isEnabled() {
        return googleCalendar != null;
    }

    public String createEvent(String calendarId, String title, LocalDate startDate, LocalDate endDate) {
        if (!isEnabled()) return null;
        try {
            Event event = buildAllDayEvent(title, startDate, endDate);
            Event created = googleCalendar.events().insert(calendarId, event).execute();
            log.info("Google Calendar 이벤트 생성 완료: calendarId={}, eventId={}", calendarId, created.getId());
            return created.getId();
        } catch (Exception e) {
            log.error("Google Calendar 이벤트 생성 실패: calendarId={}, title={}", calendarId, title, e);
            return null;
        }
    }

    public String updateEvent(String calendarId, String eventId, String title, LocalDate startDate, LocalDate endDate) {
        if (!isEnabled()) return null;
        try {
            Event event = buildAllDayEvent(title, startDate, endDate);
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

    // AIDEV-NOTE: Google Calendar all-day 이벤트의 endDate는 exclusive이므로 +1일 처리
    private Event buildAllDayEvent(String title, LocalDate startDate, LocalDate endDate) {
        Event event = new Event().setSummary(title);

        EventDateTime start = new EventDateTime()
                .setDate(new DateTime(startDate.toString()));
        event.setStart(start);

        LocalDate effectiveEnd = (endDate != null ? endDate : startDate).plusDays(1);
        EventDateTime end = new EventDateTime()
                .setDate(new DateTime(effectiveEnd.toString()));
        event.setEnd(end);

        return event;
    }
}
