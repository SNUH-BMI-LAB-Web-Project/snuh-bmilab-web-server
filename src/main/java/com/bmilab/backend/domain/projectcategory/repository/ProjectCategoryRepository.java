package com.bmilab.backend.domain.projectcategory.repository;

import com.bmilab.backend.domain.projectcategory.entity.ProjectCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectCategoryRepository extends JpaRepository<ProjectCategory, Long> {
    boolean existsByName(String name);
}
