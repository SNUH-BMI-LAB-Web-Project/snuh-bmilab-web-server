package com.bmilab.backend.domain.seminar.controller;

import com.bmilab.backend.domain.seminar.dto.request.CreateSeminarRequest;
import com.bmilab.backend.domain.seminar.dto.request.UpdateSeminarRequest;
import com.bmilab.backend.domain.seminar.dto.response.SeminarFindAllResponse;
import com.bmilab.backend.domain.seminar.dto.response.SeminarResponse;
import com.bmilab.backend.domain.seminar.enums.SeminarLabel;
import com.bmilab.backend.domain.seminar.service.SeminarService;
import com.bmilab.backend.global.security.UserAuthInfo;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seminars")
@RequiredArgsConstructor
public class SeminarController implements SeminarApi {
    private final SeminarService seminarService;

    @Override
    @GetMapping("/calendar")
    public ResponseEntity<SeminarFindAllResponse> getSeminarsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return ResponseEntity.ok(seminarService.getSeminarsByDateRange(startDate, endDate));
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<SeminarFindAllResponse> searchSeminars(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) SeminarLabel label,
            @PageableDefault(size = 10, sort = "startDate", direction = Sort.Direction.DESC)
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(seminarService.searchSeminars(keyword, label, pageable));
    }

    @Override
    @GetMapping("/{seminarId}")
    public ResponseEntity<SeminarResponse> getSeminarById(@PathVariable Long seminarId) {
        return ResponseEntity.ok(seminarService.getSeminarById(seminarId));
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> createSeminar(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid CreateSeminarRequest request
    ) {
        seminarService.createSeminar(userAuthInfo.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PutMapping("/{seminarId}")
    public ResponseEntity<Void> updateSeminar(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long seminarId,
            @RequestBody @Valid UpdateSeminarRequest request
    ) {
        seminarService.updateSeminar(userAuthInfo.getUserId(), seminarId, request);
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping("/{seminarId}")
    public ResponseEntity<Void> deleteSeminar(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long seminarId
    ) {
        seminarService.deleteSeminar(userAuthInfo.getUserId(), seminarId);
        return ResponseEntity.ok().build();
    }
}
