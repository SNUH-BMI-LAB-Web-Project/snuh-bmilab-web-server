package com.bmilab.backend.domain.project.controller;

import com.bmilab.backend.domain.project.dto.request.MeetingRequest;
import com.bmilab.backend.domain.project.dto.response.MeetingFindAllResponse;
import com.bmilab.backend.domain.project.service.MeetingService;
import com.bmilab.backend.global.security.UserAuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class MeetingController implements MeetingApi {
    private final MeetingService meetingService;

    @PostMapping("/{projectId}/meetings")
    public ResponseEntity<Void> createMeeting(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestBody MeetingRequest request
    ) {

        meetingService.createMeeting(userAuthInfo.getUserId(), projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{projectId}/meetings")
    public ResponseEntity<MeetingFindAllResponse> getAllMeetingsByProjectId(
            @PathVariable Long projectId
    ) {
        return ResponseEntity.ok(meetingService.getAllMeetingsByProjectId(projectId));
    }
}

