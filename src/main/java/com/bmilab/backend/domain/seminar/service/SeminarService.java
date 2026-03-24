package com.bmilab.backend.domain.seminar.service;

import com.bmilab.backend.domain.seminar.dto.request.CreateSeminarRequest;
import com.bmilab.backend.domain.seminar.dto.request.UpdateSeminarRequest;
import com.bmilab.backend.domain.seminar.dto.response.SeminarFindAllResponse;
import com.bmilab.backend.domain.seminar.dto.response.SeminarResponse;
import com.bmilab.backend.domain.seminar.entity.Seminar;
import com.bmilab.backend.domain.seminar.enums.RepeatType;
import com.bmilab.backend.domain.seminar.enums.SeminarLabel;
import com.bmilab.backend.domain.seminar.exception.SeminarErrorCode;
import com.bmilab.backend.domain.seminar.repository.SeminarRepository;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.service.UserService;
import com.bmilab.backend.global.config.GoogleCalendarConfig;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.external.calendar.GoogleCalendarService;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeminarService {
    private final SeminarRepository seminarRepository;
    private final UserService userService;
    private final GoogleCalendarService googleCalendarService;
    private final GoogleCalendarConfig googleCalendarConfig;

    public SeminarFindAllResponse getSeminarsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Seminar> seminars = seminarRepository.findAllByDateRange(startDate, endDate);
        List<SeminarResponse> responses = seminars.stream()
                .map(SeminarResponse::from)
                .toList();
        return SeminarFindAllResponse.of(responses);
    }

    public SeminarFindAllResponse searchSeminars(String keyword, SeminarLabel label, Pageable pageable) {
        Page<Seminar> seminars = seminarRepository.searchSeminars(keyword, label, pageable);
        List<SeminarResponse> responses = seminars.getContent().stream()
                .map(SeminarResponse::from)
                .toList();
        return SeminarFindAllResponse.of(responses, seminars.getTotalPages());
    }

    public SeminarResponse getSeminarById(Long seminarId) {
        Seminar seminar = getSeminar(seminarId);
        return SeminarResponse.from(seminar);
    }

    @Transactional
    public List<Long> createSeminar(Long userId, CreateSeminarRequest request) {
        User user = userService.findUserById(userId);
        validateRepeatOptions(request);

        List<LocalDate> occurrenceDates = calculateOccurrenceDates(
                request.startDate(), request.repeatType(), request.repeatEndDate()
        );

        // AIDEV-NOTE: endDate가 있으면 startDate와의 차이를 유지하여 각 반복 일정에 동일한 기간 적용
        Long daysBetween = (request.endDate() != null)
                ? ChronoUnit.DAYS.between(request.startDate(), request.endDate())
                : null;

        List<Long> seminarIds = new ArrayList<>();

        for (LocalDate occurrenceDate : occurrenceDates) {
            LocalDate occurrenceEndDate = (daysBetween != null)
                    ? occurrenceDate.plusDays(daysBetween)
                    : null;

            Seminar seminar = Seminar.builder()
                    .user(user)
                    .label(request.label())
                    .title(request.title())
                    .startDate(occurrenceDate)
                    .endDate(occurrenceEndDate)
                    .startTime(request.startTime())
                    .endTime(request.endTime())
                    .note(request.note())
                    .build();

            seminarRepository.save(seminar);

            if (googleCalendarService.isEnabled()) {
                String eventTitle = buildSeminarEventTitle(request.label(), request.title());
                String eventId = googleCalendarService.createEvent(
                        googleCalendarConfig.getSeminarCalendarId(),
                        eventTitle,
                        occurrenceDate,
                        occurrenceEndDate,
                        request.startTime(),
                        request.endTime()
                );
                seminar.updateGoogleEventId(eventId);
            }

            seminarIds.add(seminar.getId());
        }

        return seminarIds;
    }

    @Transactional
    public void updateSeminar(Long seminarId, UpdateSeminarRequest request) {
        Seminar seminar = getSeminar(seminarId);

        seminar.update(
                request.label(),
                request.title(),
                request.startDate(),
                request.endDate(),
                request.startTime(),
                request.endTime(),
                request.note()
        );

        if (googleCalendarService.isEnabled()) {
            String eventTitle = buildSeminarEventTitle(request.label(), request.title());
            if (seminar.getGoogleEventId() != null) {
                googleCalendarService.updateEvent(
                        googleCalendarConfig.getSeminarCalendarId(),
                        seminar.getGoogleEventId(),
                        eventTitle,
                        request.startDate(),
                        request.endDate(),
                        request.startTime(),
                        request.endTime()
                );
            } else {
                String eventId = googleCalendarService.createEvent(
                        googleCalendarConfig.getSeminarCalendarId(),
                        eventTitle,
                        request.startDate(),
                        request.endDate(),
                        request.startTime(),
                        request.endTime()
                );
                seminar.updateGoogleEventId(eventId);
            }
        }
    }

    @Transactional
    public void deleteSeminar(Long seminarId) {
        Seminar seminar = getSeminar(seminarId);

        if (googleCalendarService.isEnabled() && seminar.getGoogleEventId() != null) {
            googleCalendarService.deleteEvent(
                    googleCalendarConfig.getSeminarCalendarId(),
                    seminar.getGoogleEventId()
            );
        }

        seminarRepository.delete(seminar);
    }

    private Seminar getSeminar(Long seminarId) {
        return seminarRepository.findById(seminarId)
                .orElseThrow(() -> new ApiException(SeminarErrorCode.SEMINAR_NOT_FOUND));
    }

    private String buildSeminarEventTitle(SeminarLabel label, String title) {
        return "[" + label.getDescription() + "] " + title;
    }

    private void validateRepeatOptions(CreateSeminarRequest request) {
        if (request.repeatType() != null && request.repeatEndDate() == null) {
            throw new ApiException(SeminarErrorCode.INVALID_REPEAT_OPTIONS);
        }
        if (request.repeatType() == null && request.repeatEndDate() != null) {
            throw new ApiException(SeminarErrorCode.INVALID_REPEAT_OPTIONS);
        }
        if (request.repeatType() != null && !request.repeatEndDate().isAfter(request.startDate())) {
            throw new ApiException(SeminarErrorCode.INVALID_REPEAT_OPTIONS);
        }
    }

    private List<LocalDate> calculateOccurrenceDates(LocalDate startDate, RepeatType repeatType, LocalDate repeatEndDate) {
        if (repeatType == null) {
            return List.of(startDate);
        }

        List<LocalDate> dates = new ArrayList<>();
        LocalDate current = startDate;

        while (!current.isAfter(repeatEndDate)) {
            dates.add(current);
            current = switch (repeatType) {
                case WEEKLY -> current.plusWeeks(1);
                case MONTHLY -> current.plusMonths(1);
            };
        }

        return dates;
    }
}
