package com.bmilab.backend.domain.seminar.service;

import com.bmilab.backend.domain.seminar.dto.request.CreateSeminarRequest;
import com.bmilab.backend.domain.seminar.dto.request.UpdateSeminarRequest;
import com.bmilab.backend.domain.seminar.dto.response.SeminarFindAllResponse;
import com.bmilab.backend.domain.seminar.dto.response.SeminarResponse;
import com.bmilab.backend.domain.seminar.entity.Seminar;
import com.bmilab.backend.domain.seminar.enums.SeminarLabel;
import com.bmilab.backend.domain.seminar.exception.SeminarErrorCode;
import com.bmilab.backend.domain.seminar.repository.SeminarRepository;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.service.UserService;
import com.bmilab.backend.global.exception.ApiException;
import java.time.LocalDate;
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
    public Long createSeminar(Long userId, CreateSeminarRequest request) {
        User user = userService.findUserById(userId);

        Seminar seminar = Seminar.builder()
                .user(user)
                .label(request.label())
                .title(request.title())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .note(request.note())
                .build();

        return seminarRepository.save(seminar).getId();
    }

    @Transactional
    public void updateSeminar(Long userId, Long seminarId, UpdateSeminarRequest request) {
        Seminar seminar = getSeminar(seminarId);
        validateOwnerOrAdmin(userId, seminar);

        seminar.update(
                request.label(),
                request.title(),
                request.startDate(),
                request.endDate(),
                request.note()
        );
    }

    @Transactional
    public void deleteSeminar(Long userId, Long seminarId) {
        Seminar seminar = getSeminar(seminarId);
        validateOwnerOrAdmin(userId, seminar);

        seminarRepository.delete(seminar);
    }

    private Seminar getSeminar(Long seminarId) {
        return seminarRepository.findById(seminarId)
                .orElseThrow(() -> new ApiException(SeminarErrorCode.SEMINAR_NOT_FOUND));
    }

    private void validateOwnerOrAdmin(Long userId, Seminar seminar) {
        User user = userService.findUserById(userId);
        if (!seminar.getUser().getId().equals(userId) && !user.isAdmin()) {
            throw new ApiException(SeminarErrorCode.ACCESS_DENIED);
        }
    }
}
