package com.bmilab.backend.domain.projectcategory.service;

import com.bmilab.backend.domain.projectcategory.dto.request.ProjectCategoryRequest;
import com.bmilab.backend.domain.projectcategory.dto.response.ProjectCategoryFindAllResponse;
import com.bmilab.backend.domain.projectcategory.entity.ProjectCategory;
import com.bmilab.backend.domain.projectcategory.exception.ProjectCategoryErrorCode;
import com.bmilab.backend.domain.projectcategory.repository.ProjectCategoryRepository;
import com.bmilab.backend.global.exception.ApiException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectCategoryService {

    private final ProjectCategoryRepository projectCategoryRepository;

    public ProjectCategory findProjectCategoryById(Long categoryId) {
       return projectCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(ProjectCategoryErrorCode.CATEGORY_NOT_FOUND));
    }

    @Transactional
    public void createProjectCategory(ProjectCategoryRequest request) {
        String categoryName = request.name();

        validateNameIsDuplicate(categoryName);

        ProjectCategory projectCategory = ProjectCategory.builder()
                .name(categoryName)
                .build();

        projectCategoryRepository.save(projectCategory);
    }

    public ProjectCategoryFindAllResponse getAllProjectCategories() {
        List<ProjectCategory> categories = projectCategoryRepository.findAll();

        return ProjectCategoryFindAllResponse.of(categories);
    }

    private void validateNameIsDuplicate(String name) {
        if (projectCategoryRepository.existsByName(name)) {
            throw new ApiException(ProjectCategoryErrorCode.CATEGORY_NAME_DUPLICATE);
        }
    }

    @Transactional
    public void updateById(Long categoryId, ProjectCategoryRequest request) {
        ProjectCategory projectCategory = projectCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(ProjectCategoryErrorCode.CATEGORY_NOT_FOUND));

        projectCategory.update(request.name());
    }

    @Transactional
    public void deleteById(Long categoryId) {
        projectCategoryRepository.deleteById(categoryId);
    }
}
