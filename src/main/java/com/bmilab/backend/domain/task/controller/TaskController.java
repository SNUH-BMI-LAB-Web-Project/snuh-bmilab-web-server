package com.bmilab.backend.domain.task.controller;

import com.bmilab.backend.domain.research.paper.dto.response.PaperSummaryResponse;
import com.bmilab.backend.domain.research.patent.dto.response.PatentSummaryResponse;
import com.bmilab.backend.domain.research.presentation.dto.response.AcademicPresentationSummaryResponse;
import com.bmilab.backend.domain.task.dto.request.AcknowledgementUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskAgreementUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskBasicInfoUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskPeriodUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskPresentationUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskProposalUpdateRequest;
import com.bmilab.backend.domain.task.dto.request.TaskRequest;
import com.bmilab.backend.domain.task.dto.response.AcknowledgementResponse;
import com.bmilab.backend.domain.task.dto.response.TaskAgreementResponse;
import com.bmilab.backend.domain.task.dto.response.TaskBasicInfoResponse;
import com.bmilab.backend.domain.task.dto.response.TaskPeriodResponse;
import com.bmilab.backend.domain.task.dto.response.TaskPresentationResponse;
import com.bmilab.backend.domain.task.dto.response.TaskProjectSummary;
import com.bmilab.backend.domain.task.dto.response.TaskProposalResponse;
import com.bmilab.backend.domain.task.dto.response.TaskStatsResponse;
import com.bmilab.backend.domain.task.dto.response.TaskSummaryResponse;
import com.bmilab.backend.domain.task.enums.TaskStatus;
import com.bmilab.backend.domain.task.service.TaskService;
import com.bmilab.backend.domain.user.enums.Role;
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

import java.util.List;
import java.util.UUID;

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
        boolean isAdmin = userAuthInfo.getUser().getRole() == Role.ADMIN;
        taskService.updateTask(userAuthInfo.getUserId(), isAdmin, taskId, request);
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

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskSummaryResponse> getTask(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId
    ) {
        TaskSummaryResponse response = taskService.getTask(userAuthInfo.getUserId(), taskId);
        return ResponseEntity.ok(response);
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
        boolean isAdmin = userAuthInfo.getUser().getRole() == Role.ADMIN;
        taskService.updateBasicInfo(userAuthInfo.getUserId(), isAdmin, taskId, request);
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
        boolean isAdmin = userAuthInfo.getUser().getRole() == Role.ADMIN;
        taskService.updateProposal(userAuthInfo.getUserId(), isAdmin, taskId, request);
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
        boolean isAdmin = userAuthInfo.getUser().getRole() == Role.ADMIN;
        taskService.updatePresentation(userAuthInfo.getUserId(), isAdmin, taskId, request);
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
        boolean isAdmin = userAuthInfo.getUser().getRole() == Role.ADMIN;
        taskService.updateAgreement(userAuthInfo.getUserId(), isAdmin, taskId, request);
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
        boolean isAdmin = userAuthInfo.getUser().getRole() == Role.ADMIN;
        taskService.updateTaskPeriod(userAuthInfo.getUserId(), isAdmin, taskId, periodId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId
    ) {
        boolean isAdmin = userAuthInfo.getUser().getRole() == Role.ADMIN;
        taskService.deleteTask(userAuthInfo.getUserId(), isAdmin, taskId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{taskId}/files/{fileId}")
    public ResponseEntity<Void> deleteTaskFile(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId,
            @PathVariable UUID fileId
    ) {
        boolean isAdmin = userAuthInfo.getUser().getRole() == Role.ADMIN;
        taskService.deleteTaskFile(userAuthInfo.getUserId(), isAdmin, taskId, fileId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{taskId}/acknowledgement")
    public ResponseEntity<AcknowledgementResponse> getAcknowledgement(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId
    ) {
        AcknowledgementResponse response = taskService.getAcknowledgement(userAuthInfo.getUserId(), taskId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{taskId}/acknowledgement")
    public ResponseEntity<Void> saveAcknowledgement(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId,
            @RequestBody @Valid AcknowledgementUpdateRequest request
    ) {
        boolean isAdmin = userAuthInfo.getUser().getRole() == Role.ADMIN;
        taskService.saveAcknowledgement(userAuthInfo.getUserId(), isAdmin, taskId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{taskId}/projects")
    public ResponseEntity<List<TaskProjectSummary>> getTaskProjects(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId
    ) {
        List<TaskProjectSummary> response = taskService.getTaskProjects(userAuthInfo.getUserId(), taskId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{taskId}/projects/{projectId}")
    public ResponseEntity<Void> addProjectToTask(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId,
            @PathVariable Long projectId
    ) {
        taskService.addProjectToTask(userAuthInfo.getUserId(), taskId, projectId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{taskId}/projects/{projectId}")
    public ResponseEntity<Void> removeProjectFromTask(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId,
            @PathVariable Long projectId
    ) {
        taskService.removeProjectFromTask(userAuthInfo.getUserId(), taskId, projectId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{taskId}/papers")
    public ResponseEntity<List<PaperSummaryResponse>> getTaskPapers(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId
    ) {
        List<PaperSummaryResponse> response = taskService.getTaskPapers(userAuthInfo.getUserId(), taskId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{taskId}/presentations")
    public ResponseEntity<List<AcademicPresentationSummaryResponse>> getTaskPresentations(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId
    ) {
        List<AcademicPresentationSummaryResponse> response = taskService.getTaskPresentations(userAuthInfo.getUserId(), taskId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{taskId}/patents")
    public ResponseEntity<List<PatentSummaryResponse>> getTaskPatents(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long taskId
    ) {
        List<PatentSummaryResponse> response = taskService.getTaskPatents(userAuthInfo.getUserId(), taskId);
        return ResponseEntity.ok(response);
    }
}
