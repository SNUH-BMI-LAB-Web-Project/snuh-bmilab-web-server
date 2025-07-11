package com.bmilab.backend.domain.project.controller;

import com.bmilab.backend.domain.project.dto.request.ExternalProfessorRequest;
import com.bmilab.backend.domain.project.dto.response.ExternalProfessorFindAllResponse;
import com.bmilab.backend.domain.project.service.ProjectService;
import com.bmilab.backend.global.annotation.OnlyAdmin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@OnlyAdmin
@RestController
@RequestMapping("/admin/projects")
@RequiredArgsConstructor
public class AdminProjectController implements AdminProjectApi{

    private final ProjectService projectService;

    @PostMapping("/external-professors")
    public ResponseEntity<Void> createExternalProfessor(@RequestBody @Valid ExternalProfessorRequest request) {
        projectService.createExternalProfessor(request);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/external-professors/{professorId}")
    public ResponseEntity<Void> updateExternalProfessor(
            @PathVariable Long professorId,
            @RequestBody @Valid ExternalProfessorRequest request
    ) {
        projectService.updateExternalProfessor(professorId, request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/external-professors")
    public ResponseEntity<ExternalProfessorFindAllResponse> getAllExternalProfessors() {
        return ResponseEntity.ok(projectService.getAllExternalProfessors());
    }

    @DeleteMapping("/external-professors/{professorId}")
    public ResponseEntity<Void> deleteExternalProfessor(@PathVariable Long professorId) {
        projectService.deleteExternalProfessor(professorId);

        return ResponseEntity.ok().build();
    }
}
