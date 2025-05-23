package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.project.entity.Project;
import com.bmilab.backend.domain.project.entity.ProjectFile;
import com.bmilab.backend.domain.project.entity.ProjectFileId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectFileRepository extends JpaRepository<ProjectFile, ProjectFileId> {
    Optional<ProjectFile> findByProjectAndFileInformation(Project project, FileInformation file);

    List<ProjectFile> findAllByProjectId(Long projectId);
}
