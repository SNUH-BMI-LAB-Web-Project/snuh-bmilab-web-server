package com.bmilab.backend.domain.project.controller;

import com.bmilab.backend.domain.project.dto.condition.ProjectFilterCondition;
import com.bmilab.backend.domain.project.dto.request.ProjectCompleteRequest;
import com.bmilab.backend.domain.project.dto.request.ProjectRequest;
import com.bmilab.backend.domain.project.dto.response.ProjectDetail;
import com.bmilab.backend.domain.project.dto.response.ProjectFileFindAllResponse;
import com.bmilab.backend.domain.project.dto.response.ProjectFindAllResponse;
import com.bmilab.backend.domain.project.dto.response.SearchProjectResponse;
import com.bmilab.backend.domain.project.dto.response.UserProjectFindAllResponse;
import com.bmilab.backend.domain.project.enums.ProjectStatus;
import com.bmilab.backend.domain.project.service.ProjectService;
import com.bmilab.backend.domain.report.dto.response.ReportFindAllResponse;
import com.bmilab.backend.global.security.UserAuthInfo;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController implements ProjectApi {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Void> createNewProject(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody ProjectRequest request
    ) {

        projectService.createNewProject(userAuthInfo.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{projectId}/files/{fileId}")
    public ResponseEntity<Void> deleteProjectFile(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @PathVariable UUID fileId
    ) {

        projectService.deleteProjectFile(userAuthInfo.getUserId(), projectId, fileId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{projectId}/files")
    public ResponseEntity<ProjectFileFindAllResponse> getAllProjectFiles(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId
    ) {

        return ResponseEntity.ok(projectService.getAllProjectFiles(userAuthInfo.getUserId(), projectId));
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<Void> updateProject(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestBody ProjectRequest request
    ) {

        projectService.updateProject(userAuthInfo.getUserId(), projectId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{projectId}/complete")
    public ResponseEntity<Void> completeProject(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestBody ProjectCompleteRequest request
    ) {

        projectService.completeProject(userAuthInfo.getUserId(), projectId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{projectId}/reports")
    public ResponseEntity<ReportFindAllResponse> getReportsByProject(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String keyword
    ) {

        return ResponseEntity.ok(projectService.getReportsByProject(
                userAuthInfo.getUserId(),
                projectId,
                userId,
                startDate,
                endDate,
                null
        ));
    }

    @GetMapping
    public ResponseEntity<ProjectFindAllResponse> getAllProjects(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long leaderId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) ProjectStatus status,
            @RequestParam(required = false) String pi,
            @RequestParam(required = false) String practicalProfessor,
            @PageableDefault(size = 10, sort = "endDate", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(projectService.getAllProjects(
                userAuthInfo.getUserId(),
                search,
                ProjectFilterCondition.of(leaderId, categoryId, status, pi, practicalProfessor),
                pageable
        ));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetail> getProjectById(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId
    ) {

        return ResponseEntity.ok(projectService.getProjectDetailById(userAuthInfo.getUserId(), projectId));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProjectById(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId
    ) {

        projectService.deleteProjectById(userAuthInfo.getUserId(), projectId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<SearchProjectResponse> searchProject(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false, defaultValue = "true") boolean all,
            @RequestParam(required = false) String keyword
    ) {

        return ResponseEntity.ok(projectService.searchProject(userAuthInfo.getUserId(), all, keyword));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserProjectFindAllResponse> getUserProjects(@PathVariable Long userId) {

        return ResponseEntity.ok(projectService.getUserProjects(userId));
    }
}
