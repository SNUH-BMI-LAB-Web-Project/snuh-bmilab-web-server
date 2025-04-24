package com.bmilab.backend.domain.project.controller;

import com.bmilab.backend.domain.project.dto.request.ProjectCompleteRequest;
import com.bmilab.backend.domain.project.dto.request.ProjectRequest;
import com.bmilab.backend.domain.project.dto.response.ProjectDetail;
import com.bmilab.backend.domain.project.dto.request.ProjectFileRequest;
import com.bmilab.backend.domain.project.dto.response.ProjectFindAllResponse;
import com.bmilab.backend.domain.project.service.ProjectService;
import com.bmilab.backend.global.security.UserAuthInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController implements ProjectApi {
    private final ProjectService projectService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createNewProject(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestPart(required = false) List<MultipartFile> files,
            @RequestPart ProjectRequest request
    ) {

        projectService.createNewProject(userAuthInfo.getUserId(), files, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping(value = "/{projectId}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addProjectFile(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestPart MultipartFile file
    ) {

        projectService.addProjectFile(userAuthInfo.getUserId(), projectId, file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{projectId}/files")
    public ResponseEntity<Void> deleteProjectFile(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId,
            @RequestBody ProjectFileRequest request
    ) {

        projectService.deleteProjectFile(userAuthInfo.getUserId(), projectId, request);
        return ResponseEntity.ok().build();
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

    @GetMapping
    public ResponseEntity<ProjectFindAllResponse> getAllProjects(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "0") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(projectService.getAllProjects(pageNo, size, search));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetail> getProjectById(@PathVariable Long projectId) {

        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProjectById(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long projectId
    ) {

        projectService.deleteProjectById(userAuthInfo.getUserId(), projectId);
        return ResponseEntity.ok().build();
    }
}
