package com.bmilab.backend.domain.projectcategory.controller;

import com.bmilab.backend.domain.projectcategory.dto.request.ProjectCategoryRequest;
import com.bmilab.backend.domain.projectcategory.dto.response.ProjectCategoryFindAllResponse;
import com.bmilab.backend.domain.projectcategory.service.ProjectCategoryService;
import com.bmilab.backend.global.annotation.OnlyAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@OnlyAdmin
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/project-categories")
public class AdminProjectCategoryController implements AdminProjectCategoryApi {

    private final ProjectCategoryService projectCategoryService;

    @PostMapping
    public ResponseEntity<Void> createProjectCategory(@RequestBody ProjectCategoryRequest request) {
        projectCategoryService.createProjectCategory(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ProjectCategoryFindAllResponse> getAllProjectCategories() {
        return ResponseEntity.ok(projectCategoryService.getAllProjectCategories());
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Void> updateProjectCategory(
            @PathVariable Long categoryId,
            @RequestBody ProjectCategoryRequest request
    ) {
        projectCategoryService.updateById(categoryId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long categoryId) {
        projectCategoryService.deleteById(categoryId);

        return ResponseEntity.ok().build();
    }
}
