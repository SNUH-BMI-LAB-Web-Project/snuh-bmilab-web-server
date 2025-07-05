package com.bmilab.backend.domain.project.repository;

import com.bmilab.backend.domain.file.entity.FileInformation;
import com.bmilab.backend.domain.project.entity.ProjectFile;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectFileRepository extends JpaRepository<ProjectFile, UUID> {
    Optional<ProjectFile> findByFileInformation(FileInformation file);

    @Query(
            "SELECT pf FROM ProjectFile pf "
          + "WHERE pf.fileInformation.domainType = com.bmilab.backend.domain.file.enums.FileDomainType.PROJECT "
          + "AND pf.fileInformation.entityId = :projectId"
    )
    List<ProjectFile> findAllByProjectId(@Param("projectId") Long projectId);

    @Query(
            "SELECT pf FROM ProjectFile pf "
                    + "WHERE pf.fileInformation.domainType = com.bmilab.backend.domain.file.enums.FileDomainType.PROJECT "
                    + "AND pf.fileInformation.entityId = :projectId "
                    + "AND pf.type = com.bmilab.backend.domain.project.enums.ProjectFileType.IRB"
    )
    List<ProjectFile> findAllIrbFilesByProjectId(@Param("projectId") Long projectId);

    @Query(
            "SELECT pf FROM ProjectFile pf "
                    + "WHERE pf.fileInformation.domainType = com.bmilab.backend.domain.file.enums.FileDomainType.PROJECT "
                    + "AND pf.fileInformation.entityId = :projectId "
                    + "AND pf.type = com.bmilab.backend.domain.project.enums.ProjectFileType.DRB"
    )
    List<ProjectFile> findAllDrbFilesByProjectId(@Param("projectId") Long projectId);
}
