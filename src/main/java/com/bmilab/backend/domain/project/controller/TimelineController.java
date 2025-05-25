package com.bmilab.backend.domain.project.controller;

import com.bmilab.backend.domain.project.dto.request.TimelineRequest;
import com.bmilab.backend.domain.project.dto.response.TimelineFindAllResponse;
import com.bmilab.backend.domain.project.service.TimelineService;
import com.bmilab.backend.global.security.UserAuthInfo;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects/{projectId}/timelines")
@RequiredArgsConstructor
public class TimelineController implements TimelineApi {
    private final TimelineService timelineService;

    @PostMapping
    public ResponseEntity<Void> createTimeline(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestBody TimelineRequest request
    ) {

        timelineService.createTimeline(userAuthInfo.getUserId(), projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{timelineId}")
    public ResponseEntity<Void> updateTimeline(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @PathVariable Long timelineId,
            @RequestBody TimelineRequest request
    ) {
        timelineService.updateTimeline(userAuthInfo.getUserId(), projectId, timelineId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{timelineId}")
    public ResponseEntity<Void> deleteTimeline(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @PathVariable Long timelineId
    ) {
        timelineService.deleteTimeline(userAuthInfo.getUserId(), projectId, timelineId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<TimelineFindAllResponse> getAllTimelinesByProjectId(
            @PathVariable Long projectId
    ) {
        return ResponseEntity.ok(timelineService.getAllTimelinesByProjectId(projectId));
    }

    @DeleteMapping("/{timelineId}/files/{fileId}")
    public ResponseEntity<Void> deleteTimelineFile(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @PathVariable Long timelineId,
            @PathVariable UUID fileId
    ) {

        timelineService.deleteTimelineFile(userAuthInfo.getUserId(), projectId, timelineId, fileId);
        return ResponseEntity.ok().build();
    }
}
