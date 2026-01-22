package com.bmilab.backend.domain.research.paper.controller;

import com.bmilab.backend.domain.research.paper.dto.request.CreateJournalRequest;
import com.bmilab.backend.domain.research.paper.dto.request.UpdateJournalRequest;
import com.bmilab.backend.domain.research.paper.dto.response.JournalFindAllResponse;
import com.bmilab.backend.domain.research.paper.dto.response.JournalResponse;
import com.bmilab.backend.domain.research.paper.service.JournalService;
import com.bmilab.backend.domain.user.enums.Role;
import com.bmilab.backend.global.security.UserAuthInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/research/journals")
@RequiredArgsConstructor
public class JournalController implements JournalApi {

    private final JournalService journalService;

    @PostMapping
    public ResponseEntity<JournalResponse> createJournal(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid CreateJournalRequest request
    ) {
        JournalResponse response = journalService.createJournal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{journalId}")
    public ResponseEntity<JournalResponse> updateJournal(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long journalId,
            @RequestBody @Valid UpdateJournalRequest request
    ) {
        JournalResponse response = journalService.updateJournal(journalId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{journalId}")
    public ResponseEntity<JournalResponse> getJournal(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long journalId
    ) {
        return ResponseEntity.ok(journalService.getJournal(journalId));
    }

    @GetMapping
    public ResponseEntity<JournalFindAllResponse> getJournals(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "journalName", direction = Sort.Direction.ASC) @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(journalService.getJournals(keyword, pageable));
    }

    @DeleteMapping("/{journalId}")
    public ResponseEntity<Void> deleteJournal(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long journalId
    ) {
        boolean isAdmin = userAuthInfo.getUser().getRole() == Role.ADMIN;
        journalService.deleteJournal(userAuthInfo.getUserId(), isAdmin, journalId);
        return ResponseEntity.ok().build();
    }
}
