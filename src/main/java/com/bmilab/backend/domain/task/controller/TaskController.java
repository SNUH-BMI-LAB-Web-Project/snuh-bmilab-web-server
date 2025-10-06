package com.bmilab.backend.domain.task.controller;

import com.bmilab.backend.domain.task.dto.request.TaskRequest;
import com.bmilab.backend.domain.task.dto.response.TaskStatsResponse;
import com.bmilab.backend.domain.task.dto.response.TaskSummaryResponse;
import com.bmilab.backend.domain.task.enums.TaskStatus;
import com.bmilab.backend.domain.task.service.TaskService;
import com.bmilab.backend.global.security.UserAuthInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController implements TaskApi {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Void> createTask(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid TaskRequest request
    ) {
        taskService.createTask(userAuthInfo.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Void> updateTask(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId,
            @RequestBody @Valid TaskRequest request
    ) {
        taskService.updateTask(userAuthInfo.getUserId(), taskId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<TaskStatsResponse> getTaskStats(@AuthenticationPrincipal UserAuthInfo userAuthInfo) {
        TaskStatsResponse response = taskService.getTaskStats(userAuthInfo.getUserId());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<TaskSummaryResponse>> getAllTasks(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable
    ) {
        Page<TaskSummaryResponse> responses = taskService.getAllTasks(userAuthInfo.getUserId(), status, keyword, pageable);
        return ResponseEntity.ok(responses);
    }
}
