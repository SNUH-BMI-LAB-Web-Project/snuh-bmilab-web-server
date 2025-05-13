package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.project.entity.ProjectFile;
import com.bmilab.backend.domain.project.entity.ProjectFileId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectFileRepository extends JpaRepository<ProjectFile, ProjectFileId> {
}
