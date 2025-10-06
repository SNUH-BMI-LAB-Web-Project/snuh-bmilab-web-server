package com.bmilab.backend.domain.task.controller;

import com.bmilab.backend.domain.task.dto.request.TaskAgreementUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskBasicInfoUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskPeriodUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskPresentationUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskProposalUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskRequest;
import com.bmilab.backend.domain.task.dto.response.TaskAgreementResponse;
import com.bmilab.backend.domain.task.dto.response.TaskBasicInfoResponse;
import com.bmilab.backend.domain.task.dto.response.TaskPeriodResponse;
import com.bmilab.backend.domain.task.dto.response.TaskPresentationResponse;
import com.bmilab.backend.domain.task.dto.response.TaskProposalResponse;
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

    @GetMapping("/{taskId}/basic-info")
    public ResponseEntity<TaskBasicInfoResponse> getTaskBasicInfo(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId
    ) {
        TaskBasicInfoResponse response = taskService.getTaskBasicInfo(userAuthInfo.getUserId(), taskId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{taskId}/basic-info")
    public ResponseEntity<Void> updateBasicInfo(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId,
            @RequestBody @Valid TaskBasicInfoUpdateRequest request
    ) {
        taskService.updateBasicInfo(userAuthInfo.getUserId(), taskId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{taskId}/proposal")
    public ResponseEntity<TaskProposalResponse> getTaskProposal(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId
    ) {
        TaskProposalResponse response = taskService.getTaskProposal(userAuthInfo.getUserId(), taskId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{taskId}/proposal")
    public ResponseEntity<Void> updateProposal(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId,
            @RequestBody @Valid TaskProposalUpdateRequest request
    ) {
        taskService.updateProposal(userAuthInfo.getUserId(), taskId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{taskId}/presentation")
    public ResponseEntity<TaskPresentationResponse> getTaskPresentation(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId
    ) {
        TaskPresentationResponse response = taskService.getTaskPresentation(userAuthInfo.getUserId(), taskId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{taskId}/presentation")
    public ResponseEntity<Void> updatePresentation(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId,
            @RequestBody @Valid TaskPresentationUpdateRequest request
    ) {
        taskService.updatePresentation(userAuthInfo.getUserId(), taskId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{taskId}/agreement")
    public ResponseEntity<TaskAgreementResponse> getTaskAgreement(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId
    ) {
        TaskAgreementResponse response = taskService.getTaskAgreement(userAuthInfo.getUserId(), taskId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{taskId}/agreement")
    public ResponseEntity<Void> updateAgreement(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId,
            @RequestBody @Valid TaskAgreementUpdateRequest request
    ) {
        taskService.updateAgreement(userAuthInfo.getUserId(), taskId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{taskId}/periods/{periodId}")
    public ResponseEntity<TaskPeriodResponse> getTaskPeriod(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId,
            @PathVariable Long periodId
    ) {
        TaskPeriodResponse response = taskService.getTaskPeriod(userAuthInfo.getUserId(), taskId, periodId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{taskId}/periods/{periodId}")
    public ResponseEntity<Void> updateTaskPeriod(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId,
            @PathVariable Long periodId,
            @RequestBody @Valid TaskPeriodUpdateRequest request
    ) {
        taskService.updateTaskPeriod(userAuthInfo.getUserId(), taskId, periodId, request);
        return ResponseEntity.ok().build();
    }
}
