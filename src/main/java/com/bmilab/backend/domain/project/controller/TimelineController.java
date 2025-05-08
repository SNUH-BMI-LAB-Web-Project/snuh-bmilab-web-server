package com.bmilab.backend.domain.project.controller;

import com.bmilab.backend.domain.project.dto.request.TimelineRequest;
import com.bmilab.backend.domain.project.dto.response.TimelineFindAllResponse;
import com.bmilab.backend.domain.project.service.TimelineService;
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
public class TimelineController implements TimelineApi {
    private final TimelineService timelineService;

    @PostMapping("/{projectId}/timelines")
    public ResponseEntity<Void> createTimeline(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestBody TimelineRequest request
    ) {

        timelineService.createTimeline(userAuthInfo.getUserId(), projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{projectId}/timelines")
    public ResponseEntity<TimelineFindAllResponse> getAllTimelinesByProjectId(
            @PathVariable Long projectId
    ) {
        return ResponseEntity.ok(timelineService.getAllTimelinesByProjectId(projectId));
    }
}

