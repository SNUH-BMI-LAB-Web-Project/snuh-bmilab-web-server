package com.bmilab.backend.domain.projectcategory.controller;

import com.bmilab.backend.domain.projectcategory.dto.response.ProjectCategoryFindAllResponse;
import com.bmilab.backend.domain.projectcategory.service.ProjectCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project-categories")
@RequiredArgsConstructor
public class ProjectCategoryController implements ProjectCategoryApi {
    private final ProjectCategoryService projectCategoryService;

    @GetMapping
    public ResponseEntity<ProjectCategoryFindAllResponse> getAllProjectCategories() {
        return ResponseEntity.ok(projectCategoryService.getAllProjectCategories());
    }
}
